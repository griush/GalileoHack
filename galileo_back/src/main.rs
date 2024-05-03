#[async_std::main]
async fn main() -> Result<(), std::io::Error> {
    tide::log::start();
    let mut app = tide::new();
    app.at("/").get(|_| async { Ok("Hello, world!") });

    app.listen("0.0.0.0:8080").await?;
    Ok(())
}
