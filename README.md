# Test Quarkus reactive with panache reactive postgres on kthcloud

## Test locally

### Prerequisites

- `Docker`
- `Docker-compose`

```bash
docker compose up
```

## Test on kthcloud

### Prerequisites

- `kthcloud-cli`
- `Docker`

> !NOTE
> If quarkus starts before the db, (very likely since it is a fast native image and the db has init scripts to run). You might have to manually restart it when the db is up again.

### Linux / macOS

First:
 - login:   
    ```bash
    kthcloud login
    ```
 - open your storage manager site in your browser (should be `https://<your-kthcloud-uuid>..storage.cloud.cbh.kth.se`) if you have created a persistent volume previously you will find it [here](https://cloud.cbh.kth.se/deploy) by clicking `Manage Storage`.
 - check if it managed to steal cookies:
    ```bash
    kthcloud compose sm check
    ```
 - if not, try again. (annoying IK, working on it)

Then:

```bash
kthcloud compose up --build --non-interactive --try-volumes
```

### Windows

Create the required volume manually in the root of you kthcloud storagemanager:

> !NOTE
> This path is created by hashing the service names, if you change the names of the services you will have to use the new hash, check it with the `kthcloud compose parse` command.

```txt
6594acfa4545194b80011f6c702bcd6cb16a8aabacd4e9fad8f757ace8c450bc/data
```

```bash
kthcloud compose up --build --non-interactive
```

## Check if it worked

### Locally

Check[here](http://localhost:8080/persons) 

### kthcloud

Check:`https://<your-quarkus-service-name>.app.cloud.cbh.kth.se/persons`
