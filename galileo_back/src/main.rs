use tide::*;
use serde::*;
use serde_json::*;

#[derive(Debug, Deserialize, Serialize)]
struct GnssSatelliteData {
    // Mesurements
    svid: u64,
    constelation: u32,
    time_offset_nanos: f32,
    state: u16,
    recieved_sv_time_nanos: u64,
    recieved_sv_time_uncertanty_nanos: u64,
    cn0_db_hz: f64,
    baseband_cn0_db_hz: f64,
    pseudorange_rate_meters_per_second: f64,
    pseudorange_rate_uncertanty_meters_per_second: f64,
    accumulated_delta_range_state: u16,
    accumulated_delta_range_meters: f32,
    accumulated_delta_range_uncertanty_meters: f32,
    carrier_frequency_hz: f64,
    multipath_indicator: u16,
    agc_level_db: f32,
    code_type: String,    

    // Clock
    time_nanos: u64,
}

#[derive(Debug, Deserialize, Serialize)]
struct UserLocation {
    pub lat: f32,
    pub lon: f32,
    pub signal: f64,
}

#[async_std::main]
async fn main() -> tide::Result<()> {
    tide::log::start();

    // Setup end points and server
    let mut app = tide::new();

    app.at("/health").get(|_| async { Ok("Loko funsiona") });

    app.at("/fetch").post(|mut req: Request<()>| async move {
        let body = req.body_string().await?;

        let v: Value = serde_json::from_str(&body)?;
        
        let num_sats = v["SatelliteCount"].as_u64().unwrap();
        let mut sum:f64 = 0.0;
        for i in 0..num_sats {
            sum += v["Satellites"][i as usize]["Cn0DbHz"].as_f64().unwrap();
        }

        let signal = sum / (num_sats as f64);

        let mut res = Response::new(200);
        res.set_body(Body::from_json(&UserLocation{
            lat: -1.0,
            lon: -1.0,
            signal,
        })?);

        Ok(res)
    });

    app.listen("0.0.0.0:4321").await?;
    Ok(())
}

