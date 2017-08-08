# Intuit Craft Demo
## SBG Platform Service Craft Demonstration

### User Story: 
As part of enhancing our internal services which are available to our employees, we would like to build a Twitter like solution for our employees, where employees can tweet and have followers. 

* High-Level Requirements :
  * We have 10K employees
  * Employees can follow their colleagues, post (or tweet) messages to their followers
  * Use corporate LDAP for user Management
  * On an average, every employee will send approximately 10 messages a day to their followers
  * On the home page we need to show 100 most recent tweets. Optionally you can support pagination.
  * You are welcome to assume unspecified requirements to make it better for the customers
  * Come prepared with High-level Architecture and Design. 
  * You are expected to explain the rationale for your choice of technologies and architectural patterns

* Programming Problem
  * In the above exercise, please build a RESTful service 
  * /feed : To list 100 recent tweets for the logged in user
  * In memory database is sufficient. Optionally, you are welcome to use a persistent data store of your choice.
  * You are encouraged to take advantage of a service code-generation framework of your choice when performing this exercise. Please use industry standard coding best practices keeping in mind maintainability. 

### Demo code
The `feed-micro-jwt` folder contains two [jhipster](https://jhipster.github.io/) generated microservices and a  [jhipster](https://jhipster.github.io/) generated docker-compose configuration

* **feed-micro-jwt/app:** Backend java microservice code containing REST API endpoints for `Tweet` and `TweetAuthor` entity operations as well as the `/feed` endpoint
* **feed-micro-jwt/gateway:** Frontend AngularJS microservice code handling authentication and API routing
* **feed-micro-jwt/docker-compose:** Docker compose definitions for development microservice orchestration
* **feed-micro-jwt/schema:** Simple JHipster JDL entity schema

