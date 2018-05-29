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
The deployment of the whole _Proxima_ system is currently a complex process. If you want to deploy and run the system in your computer(s), please, contact us: we are very happy to tell you how to do it! :wink:.   
