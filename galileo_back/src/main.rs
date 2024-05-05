use tide::*;
use serde::*;
use serde_json::*;

#[derive(Debug, Deserialize, Serialize)]
struct SNRSignal {
    pub signal: f64,
}

#[derive(Debug, Deserialize, Serialize)]
struct UserLocation {
    pub lat: f64,
    pub lon: f64,
}

fn resolve_pvt(u: &mut UserLocation, v: Value) {
    // Need at least 3 satellites to determine lat and lon, you also need a 4th for altitude but
    // not today
    if v.as_array().unwrap().len() < 3 {
        *u = UserLocation {
            lat: -1.0,
            lon: -1.0,
        }
    }

    
}

#[async_std::main]
async fn main() -> tide::Result<()> {
    tide::log::start();

    // Setup end points and server
    let mut app = tide::new();

    app.at("/health").get(|_| async { Ok("Funsiona loko") });

    app.at("/fetch").post(|mut req: Request<()>| async move {
        let body = req.body_string().await?;
        println!("{:?}", body);

        let v: Value = serde_json::from_str(&body)?;

        let num_sats = v["SatelliteCount"].as_u64().unwrap();
        if num_sats == 0 {
            let mut res = Response::new(200);
            res.set_body(Body::from_json(&SNRSignal{
                signal: -1.0,
            })?);

            return Ok(res);
        }

        let mut sum:f64 = 0.0;
        for i in 0..num_sats {
            let sat = &v["Satellites"][i as usize];
            sum += sat["Cn0DbHz"].as_f64().unwrap();
        }

        let signal = sum / (num_sats as f64);

        let mut res = Response::new(200);
        res.set_body(Body::from_json(&SNRSignal{
            signal,
        })?);

        Ok(res)
    });

    app.at("/ephemeris").post(|mut req: Request<()>| async move {
        tide::log::warn!("/ephemeris request recieved <==========================>");

        let body = req.body_string().await?;
        println!("{:?}", body);
        
        let v: Value = serde_json::from_str(&body)?;

        let mut u_loc = UserLocation {
            lat: -1.0,
            lon: -1.0,
        };

        resolve_pvt(&mut u_loc, v);

        let mut res = Response::new(200);
        res.set_body(Body::from_json(&UserLocation{
            lat: 5.0,
            lon: 5.0,
        })?);
        Ok(res)
    });

    app.listen("0.0.0.0:4321").await?;
    Ok(())
}

