use tide::*;
use rand::prelude::*;
use chrono::{DateTime, Utc};
use influxdb::{Client, InfluxDbWriteable, ReadQuery};
use serde::*;

#[derive(InfluxDbWriteable)]
struct GnssMeasurement {
    time: DateTime<Utc>,
    svid: u64,
    constelation: u32,
}

#[derive(Debug, Deserialize, Serialize)]
struct UserLocation {
    pub lat: f32,
    pub lon: f32,
    pub signal: f32,
}

#[tokio::main]
async fn main() -> Result<()> {

    tide::log::start();

    // Setup InfluxDB
    let client = Client::new("http://84.247.188.251:8086", "galileo_hacker").with_auth("grius", "e231f34805d470c5");

    let read_query = ReadQuery::new("SELECT * FROM gnss_testdata");

    let read_result = client.query(read_query).await?;

    println!("{}", read_result);

    // Setup end points and server
    let mut app = tide::new();

    app.at("/health").get(|_| async { Ok("Loko funsiona") });

    app.at("/fetch").get(|_| async {
        let mut rng = thread_rng();
            let signal: f32 = rng.gen_range(0.0..60.0);

        let mut res = Response::new(200);
        res.set_body(Body::from_json(&UserLocation{
            lat: 41.0543,
            lon: 2.34254,
            signal
        })?);
        Ok(res)
    });

    app.listen("0.0.0.0:4321").await?;
    Ok(())
}

