This is Leaning of creation of SpringBoot MicroServices Based Application using Keycloak, EurekaServer, ApiGateway, H2 file based database, JPA, CircuitBreaker and WebClient Based authentication.

Start KeyCloak by:

Navigate to KeyCloak/Bin directory launch cmd and give command : kc.bat startdev

Admin USER/PASSWORD FOR KEYCLOAK: root/root

Eureka server user/password: USER/password
Then open postman and import collections from Postman_Collections_TO_import_and_Test folder

Start DiscoveryService
Start ProductService
Start OrderService
Start InventoryService
Start ApiGateway

Proceed with testing.

Download Zipkin from: https://repo1.maven.org/maven2/io/zipkin/zipkin-server/3.5.1/zipkin-server-3.5.1-exec.jar

launch cmd from folder containing zipkin-server-3.5.1-exec.jar and give the following commands:

java -jar zipkin-server-3.5.1-exec.jar

