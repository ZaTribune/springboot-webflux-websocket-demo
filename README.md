# Overview

The purpose of this application is to showcase a mechanism that tracks 
 user UI interactions on a payment page.  
So imagine a situation where you have a percentage of users 
whose transactions are being canceled/rejected within your provided payment page.  
We're interested in finding out what was happening before those transactions were canceled/rejected 
while a user was surfing that particular payment page.

![](design.drawio.svg)

## Features

| Frontend | Backend     | Database | 
|----------|-------------|----------|
| React JS | Spring Boot | MongoDB  |


* We have two types of connections:
  * Traditional Restful connection.
  * Tracker events are sent from the UI to BE via a websocket connection.
* This websocket connection is intended to be <u>separate & dedicated & always open</u> for each payment window.
* The backend side including websocket connection is implemented reactively via `spring:webflux`.
* Each time a user loads the payment page, there'll be a payment session assigned to it.
* Payment sessions are created with id of the `websocket's session id` (managed by spring framework).
* Exposing tracking data via a `REST` API.
* `MongoDB` is our choice as a data-source for its nice archiving capabilities.