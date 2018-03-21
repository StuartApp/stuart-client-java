# Stuart Java Client
For a complete documentation of all endpoints offered by the Stuart API, you can visit [Stuart API documentation](https://stuart.api-docs.io).

## Install
If you're using **Maven**, add the following dependency to your `pom.xml` file:

``` xml
<dependency>
  <groupId>com.stuart</groupId>
  <artifactId>stuartclientjava</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage

### Initialize HTTP client

```java
Environment environment = Environment.SANDBOX;
String apiClientId = "c6058849d0a056fc743203acb8e6a850dad103485c3edc51b16a9260cc7a7689" // can be found here: https://admin-sandbox.stuart.com/client/api
String apiClientSecret = "aa6a415fce31967501662c1960fcbfbf4745acff99acb19dbc1aae6f76c9c618" // can be found here: https://admin-sandbox.stuart.com/client/api
Authenticator authenticator = new Authenticator(environment, apiClientId, apiClientSecret);

HttpClient httpClient = new HttpClient(authenticator);
```

### Custom request