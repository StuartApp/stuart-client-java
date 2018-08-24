[ ![Codeship Status for StuartApp/stuart-client-java](https://app.codeship.com/projects/d4832110-0f57-0136-454f-1e7ea711f6fc/status?branch=master)](https://app.codeship.com/projects/282509)

![Nexus](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.stuartapp/stuart-client-java.svg)

# Stuart Java Client
For a complete documentation of all endpoints offered by the Stuart API, you can visit [Stuart API documentation](https://stuart.api-docs.io).

## Install
If you're using **Maven**, add the following dependency to your `pom.xml` file:

``` xml
<dependency>
  <groupId>com.github.stuartapp</groupId>
  <artifactId>stuart-client-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage

### Initialize HTTP client

```java
Environment environment = Environment.SANDBOX;
String apiClientId = "c6058849d0a056fc743203acb8e6a850dad103485c3edc51b16a9260cc7a7689"; // can be found here: https://admin-sandbox.stuart.com/client/api
String apiClientSecret = "aa6a415fce31967501662c1960fcbfbf4745acff99acb19dbc1aae6f76c9c618"; // can be found here: https://admin-sandbox.stuart.com/client/api
Authenticator authenticator = new Authenticator(environment, apiClientId, apiClientSecret);

HttpClient httpClient = new HttpClient(authenticator);
```

### Custom request

```java
public void createAJobExample() {

  JsonObject job = new JsonObject();
  job.addProperty("transport_type", "bike");

  JsonArray pickups = new JsonArray();
  JsonObject pickup = buildLocation(
          "46 Boulevard Barbès, 75018 Paris",
          "Wait outside for an employee to come.",
          "Martin",
          "Pont",
          "+33698348756",
          "KFC Paris Barbès"
  );
  pickups.add(pickup);

  JsonArray dropoffs = new JsonArray();
  JsonObject dropoff = buildLocation(
          "156 rue de Charonne, 75011 Paris",
          "code: 3492B. 3e étage droite. Sonner à Durand.",
          "Alex",
          "Durand",
          "+33634981209",
          "Durand associates."
  );
  dropoff.addProperty("client_reference", "reference-id-01");
  dropoffs.add(dropoff);

  JsonObject root = new JsonObject();
  job.add("pickups", pickups);
  job.add("dropoffs", dropoffs);
  root.add("job", job);

  ApiResponse apiResponse = httpClient.performPost("/v2/jobs", root.toString());
}

public JsonObject buildLocation(String address, String comment, String firstname, String lastname, String phone, String company) {
  JsonObject location = new JsonObject();

  location.addProperty("address", address);
  location.addProperty("comment", comment);

  JsonObject contact = new JsonObject();
  location.add("contact", contact);

  contact.addProperty("firstname", firstname);
  contact.addProperty("lastname", lastname);
  contact.addProperty("phone", phone);
  contact.addProperty("company", company);

  return location;
}

```
