# ITX BACKEND REST API

- [Exam Objetives](./docs/Objetives.md)

## Application

By default, when the REST API container has been built and running, the `./apirest` content is binded to the container at `/var/www` directory and a **Suervisor** service will execute the application. These setting are in the platform respository.

Once the container is built and running, you can access to it by
```bash
$ make apirest-ssh
```

If you want to make any change into the application, you should stop the Supervisor service
```bash
$ supervisorctl stop java-dev
```

So, you can run the following commands

### Developing new featuresor maintain the API

First start up or re-install dependencies
```bash
$ mvn -U clean package
```

Start REST API
```bash
$ mvn spring-boot:run
```

Tests API
```bash
/var/www $ mvn -U clean test
```
<br>

## K6 Tests

From this project location `./` or from upper directory inside the platforms repository, you can run the K6 tests by
```bash
$ make k6-tests-run
```
<br>


## License

This project is open-sourced under the [Apache license](LICENSE).

<!-- FOOTER -->
<br>

---

<br>