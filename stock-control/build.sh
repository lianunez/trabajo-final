cd java_stock_control
mvn clean install
sleep 10
cd ..
docker compose up --build --force-recreate