use tide::*;

#[async_std::main]
async fn main() -> Result<()> {
    tide::log::start();

    let mut app = tide::new();

    app.at("/health").get(|_| async { Ok("Loko funsiona") });

    app.listen("0.0.0.0:4321").await?;
    Ok(())
}

