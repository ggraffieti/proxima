# Proxima
_[ˈprɒksɪmə] a flare star in the constellation Centaurus that is the nearest star to the sun. It is a red dwarf of very low magnitude. Distance: 4.3 light years._

Like the star, which is near but invisible, our goal is to offer a location-based system where the interaction is based on the proximity of devices and at the same time every operation is invisible to users.

The increasing diffusion of low cost computational devices, and the spread of _Internet of Things_ technologies have split the computation into single small devices with low computational power. The interaction between these devices and humans have become a pervasive issue in the field of computer science.

Nowadays humans still play an active role in these interactions: humans have to do an explicit action to start the interaction. That needs to change.

With Proxima every interaction can be performed in an automatic way and in a virtual space. In a pervasive scenario, devices have low computational power, so they can't perform complex tasks. In Proxima the role of devices is restricted to the discovery of near devices, and the activation of an interaction with them. The computation is performed in a virtual space, and results are returned to devices.

Every user of the system (human or device) is identified by an identifier, which can be discovered by near users, and used to perform some kind of actions, like data retrival, take advantage of a service or coordination.

Currently only the _first aid service_ has been developed, which can be used in emegency situations by rescuers to retrieve medical data of patients. Nonetheless, the number of services in the platform is potentially endless: a new service can be added anytime, even when the system is running.

## Project structure

#### Proxima domain model
- Path <code>/ProximaDomainModel</code>

This directory contains the model of requirements of the first aid service. This model is made using the _QActor_ language.

#### Proxima problem analysis model
- Path <code>/ProximaProblemAnalysisModel</code>

This directory contains the model that formalizes the logical architecture of the first aid service, developed after the problem analysis phase. This model is made using the _QActor_ language.

#### DNS sevice
- Path <code>/DnsService</code>

This directory contains the code of the Proxima DNS service. This service is similar to the Internet DNS, and allows the resolution of service names to physical references.

#### First Aid Service
- Path <code>/FirstAidService</code>

This directory contains the code of the first aid sevice. This service manages the access to medical data, in order to make it available only to authorized rescuers.

#### Front server
- Path <code>/FrontServer</code>

This directory contains the code of the front server of the _Proxima_ infrastructure. Every request has to be sent to this component, that processes it and routes it to the right service component.

#### Users authorizations service
- Path <code>/UsersAuthorizationsService</code>

This directory contains the code of the service that authorize the interaction of two users, based on their preferences.

#### Logger
- Path <code>/Logger</code>

This directory contains the code of the logger, that logs every data access, in order for every access to be accountable.

#### App
- Path <code>/App</code>

This directory contains the code of the Android app used by rescuers in order to retrieve medical data of patients.


## Note for developers
Every contribution to this project is very welcome! If you want to contribute to this project, please, contact the team.

## Deployment
**Note**: every component can be deployed in a different machine. <br />
:warning:  please, follow the order below for a  smooth deployment.

#### DNS
1. Download the `proximaDNS.jar` from [here](https://github.com/ggraffieti/proxima/releases/download/v1.0/proximaDNS.jar).
2. Download the `address.json` file from `proxima/DnsService` and put it in the same directory of the jar file.
3. Open the file and substitute the IP field with your machine IP address.
4. Pull up a terminal and type `java -jar proximaDNS.jar`. The DNS service should start, and listen on port 1406.

#### User Authorization Service
1. Assure that you have `npm`, `mongo`, `mongorestore` and `tsc` (typescript) installed on your system.
2. Now restore the database from dumped data, located in `db/`
  - Open a shell, in the  `db` directory, and type `mongod -dbpath <your-mongo-dir>` If you don't have a mongo directory, create it.
  - Open another shell in the same directory, and type `mongorestore --drop -d proximaUsersAuthorizations ./` for restoring the database. The database name **has to be** proximaUsersAuthorizations.
3. The database is restored, now open a shell in the service root directory and type `npm install` to install dependencies. If something doesn't work, or you are unable to download dependencies, please, open an issue or contact one member of the team.
4. Open a shell in the service root directory, and type `tsc` to compile the typescript code in javascript.
5. After that open a shell in the service root directory directory and type `npm start`. The server should start at the port 8436. If not open an issue or contact one member of the team (probably you have to compile typescript in javascript. Use `tcs` command or your favorite editor).

#### Front Server
1. First you have to create a fake HTTPS certificate for the server. You can use any tool you want to create certificate, just be sure to use a 2048 bit private key and a x509 format for the certificate. Below we describe the process using `openssl`.
  - Assure you have `openssl` installed.
  - Pull up a terminal, and generate the private key with `openssl genrsa -des3 -out HTTPSkey.key 2048`. Make sure you remember the password inserted.
  - Generate a new  private key which do not require a passwork, with `openssl rsa -passin pass:<your-key-password> -in HTTPSkey.key -out HttpsKey.key`
  - Create a new certificate request with `openssl req -new -key HttpsKey.key -out HttpsCert.csr`. In the `Common Name` field enter the IP address of the machine running the Front Server. You can leave the other fields blank.
  - Now sign your certificate with your own private key, with `openssl x509 -req -sha256 -days 365 -in HttpsCert.csr -signkey HttpsKey.key -out HttpsCert.crt`.
2. Once you have created the certificate move the certificate and the private key to `FrontServer/res/`. Please replace the default key and certificate.
3. In the same directory, open the file `ServicesAddresses.json` and edit `ip` and `port` of `usersAuthorizationsService` and `dnsService`.
4. Open a shell in the service root directory and type `npm install` to install dependencies.
5. In the same shell, type `tsc` to compile typescript in javascript. A new directory called `build` will appear.
6. Run the service with `npm start`. The front server should listen on port 6041.

#### Logger
1. Open a shell in the logger service root directory and type `npm install` to install dependencies.
2. In the same shell type `tsc` to compile typescript in javascript.
3. Run the service with `npm start`. A logger service should start and listen on port 6666.

#### First Aid Service
1. Assure that you have `npm`, `mongo`, `mongorestore` and `tsc` (typescript) installed on your system.
2. Now restore the databases from dumped data, located in `db/`
  - Open a shell, and type `mongod -dbpath <your-mongo-dir>` If you don't have a mongo directory, create it.
  - Open another shell in the `db/` directory, and type `mongorestore --drop -d proximaFirstAid ./proximaFirstAid` for restoring the medical data database. The database name **has to be** proximaFirstAid.
  - In the same shell type `mongorestore --drop -d proximaRescuersKeys ./proximaRescuersKeys` for restoring the rescuers keys database. The database name **has to be** proximaRescuersKeys.
3. Now go to `resources/` directory and open `serverConfiguration.prox` file. Edit the configuration file inserting the ip addresses and ports of dns service and logger service.
4. Now open a shell in the service root directory and type `npm install` to install dependencies. If something doesn't work, or you are unable to download dependencies, please, open an issue or contact one member of the team.
5. Open a shell in the service root directory, and type `tsc` to compile the typescript code in javascript.
6. After that open a shell in the service root directory directory and type `npm start`. The server should start at the port 9876 and 9877. If not open an issue or contact one member of the team (probably you have to compile typescript in javascript. Use `tcs` command or your favorite editor).

#### App
1. Open the project in `App/` with an Android editor (we strongly recommend [Android Studio](https://developer.android.com/studio/)).
2. Open the class `app/src/main/java/org/gammf/proxima/util/ServerParameters.java` and edit the static fields `SERVER_IP` and `SERVER_PORT` with the Front Server IP and port.
3. Copy the certificate generated for the Front Server in `App/app/src/main/assets/` and rename it in `proxima_certificate.crt`. If the default certificate is present overwrite it.
4. Build the application and generate the `.apk` file.
5. Install the `.apk` file in an Android device (the Android device must have NFC capability and it must be compatible with the NFC tags used) and run the application.

## Extend the system

#### Write in a new tag
- We only support the NDEF tag format. During the developing phase we used Mifare Classic 1K tags.
- For write data inside tags we use the [NFC Tools](https://play.google.com/store/apps/details?id=com.wakdev.wdnfc&hl=it) application which is really useful.
- The _Proxima_ application only supports text plain data inside tags, so be sure to write on tags following this mime.
- We do not have any constrain on identifier format, so you can use your own rules for identifiers, just follow the rule above, and remember to update services consequently.

#### Add a new patient
Once you have the identifier for the patient you have to create their medical profile and update the User Authorization Service.
1. Update the collection `medicalrecords` inside the database `proximaFirstAid`, and insert patient medical data following the format specified in the report.
2. Add an entry for the patient in the `usersauthorization` collection inside the database `proximaUsersAuthorization`. Make sure to authorize the patient for the service `proxima.medical.firstAid`.

#### Add a new rescuer
Once you have the identifier for the rescuer you have to create their private key and update the First Aid Service.
1. Create a new RSA private (and public) key for the rescuer. You can use any tool you want, just make sure that the private key is 2048 bit long and the public key is in the PEM format.
  - With `openssl` open a shell, and type `openssl genrsa -out rsa_2048_priv.pem 2048` to create the private key.
  - In the same shell type `openssl rsa -in rsa_2048_priv.pem -out rsa_2048_pub.pem -outform PEM -pubout` for creating the public key (in PEM format).
2. Insert the public key in the collection `keys` inside the database `proximaRescuersKeys`. Make sure to associate it with the correct rescuer. If you have the First Aid Service running you can also add the key dynamically, with a request formatted as described in [this page](https://app.swaggerhub.com/apis/gmp/ProximaFirstAidService/1.0.0) (keyManagement `/addKey`).
3. Include the private key in the app that will be ran in the rescuer device. Put the key in `App/app/src/main/assets/`, and rename it `private_key.der`. If another private key is present overwrite it.
4. Add an entry for the rescuer in the collection `rescuersworkschedules` in the database `proximaFirstAid`, following the forma specified in the report.
