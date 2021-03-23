## A demo app with kotlin coroutines, spring webflux, r2dbc

### Run the app

```sh
./gradlew clean bootRun
```

### Test some endpoints

```sh
http 'http://localhost:9090/cities'
http POST 'http://localhost:9090/cities' name="city1" country="USA" pop="100000"
http 'http://localhost:9090/cityids'
```