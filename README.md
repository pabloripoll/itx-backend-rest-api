# ITX BACKEND REST API

- [Exam Objetives](./docs/Objetives.md)
- [Application Set Up](#application-set-up)
- [Grafana K6 Tests](#grafa-k6-tests)

## <a id="application-set-up"></a>Application Set Up

By default, when the REST API container is built and running, the `./apirest` content is binded to the container at `/var/www` directory.

```bash
$ tree -a ./platforms/nginx-java-21

./platforms/nginx-java-21
├── LICENSE
├── Makefile
├── README.md
└── docker
    ├── .dockerignore
    ├── .env
    ├── .env.example
    ├── .gitignore
    ├── Dockerfile
    ├── config
    │   ├── crontab
    │   ├── java
    │   │   ├── conf.d
    │   │   │   └── .gitkeep
    │   │   └── conf.d-sample
    │   │       └── default.conf
    │   ├── nginx
    │   │   ├── conf.d
    │   │   │   ├── .gitkeep
    │   │   │   └── default.conf
    │   │   ├── conf.d-sample
    │   │   │   └── default.conf
    │   │   └── nginx.conf
    │   ├── ownerships.sh
    │   └── supervisor
    │       ├── conf.d
    │       │   ├── .gitkeep
    │       │   └── nginx.conf
    │       ├── conf.d-sample
    │       │   ├── java-dev.conf # dev mode
    │       │   ├── java-jar.conf # jar
    │       │   └── nginx.conf
    │       └── supervisord.conf
    └── docker-compose.yml
```

Once the container is built and running, you can access to it by
```bash
$ make apirest-ssh
```

### Developing new features or maintain the API inside the container

Start REST API in development mode *(faster way to test the application)*
```bash
/var/www $ mvn spring-boot:run
```

Tests API to be compiled
```bash
/var/www $ mvn -U clean test
```

First start up *-or re-install dependencies-*. It creates a jar file at `./target/app.jar`
```bash
/var/www $ mvn -U clean package -DskipTests
```

**Optionally**: It is installed **Supervisor** to keep alive the process inside the container, like NGINX. A service can be added into the container so you can option for running in dev mode or a jar file. These setting are located in the platform respository.

Once you decided in which state you will keep the application alive, go to `./platforms/nginx-java-21/docker/config/supervisor` and move the services you set from `conf.d-sample` to `conf.d`

Then, placed at `./platforms/nginx-java-21/` execute the supervisord recipe
```bash
$ make supervisord-update
```

If you want to make any change into the application, you should stop the Supervisor service
```bash
$ sudo supervisorctl stop java-jar
```
<br>

## <a id="grafa-k6-tests"></a>Grafana K6 Tests

From this project location `./` or from upper directory inside the platforms repository *(outside of any container)*, you can run the K6 tests by
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