REM #### command for mvn and docker build ####
@echo on
cd api-gateway
REM ## work on api-gateway folder
call mvn clean package -Dmaven.test.skipn 
call docker build -f Dockerfile -t emart-api-gateway .

@echo on
cd ../discovery-server
REM ## work on discovery-server folder
call mvn clean package -Dmaven.test.skip
call docker build -f Dockerfile -t emart-discovery-api .

@echo on
cd ../inventory-service
REM ## work on inventory-server folder 
call mvn clean package -Dmaven.test.skip
call docker build -f Dockerfile -t emart-inventory-api .

@echo on
cd ../order-service
REM ## work on order-server folder
call mvn clean package -Dmaven.test.skip
call docker build -f Dockerfile -t emart-order-api .

@echo on
cd ../product-service
REM ## work on product-server folder
call mvn clean package -Dmaven.test.skip
call docker build -f Dockerfile -t emart-product-api .

@echo on
cd ../notification-service
REM ## work on notification-server folder
call mvn clean package -Dmaven.test.skip
call docker build -f Dockerfile -t emart-notification-api .

@echo on
cd ..
REM ## back to emart foler

pause

