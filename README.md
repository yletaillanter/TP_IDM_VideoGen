By Hugo Vallée & Yoann Le Taillanter

# Videogen

Videogen is a web configurator from a textual specification. You can generate playlists of video. It is a project of Master degree in model engineering. You will find on this readme: 

  - Motivation
  - Technologies
  - Architecture
  - Deployment

## Motivation

This project takes place in the model engineering module in Master 2 MIAGE at ISTIC in University of Rennes1. The main goal is to use metamodel to generate alternatives of playlist of videos. The website is here to test the project and make a pratical application.

Different steps were required to achieve this project
* Define the grammar
* Model to text
* Define a playlist model
* Model to Model to text
* Jhipster Website

## Technologies

We used many technologies, you can find the list below:

* [Xtext] - Generate grammar
* [Xtend] - Language
* [EMF] - Metamodel with Ecore and Genmodel
* [JHIPSTER] - A Yeoman generator 
* [Flowplayer] - An HTML5 video player

## Architecture

### Models
   Contains the Videogen model + YOGO grammar and the Playlist Model
   
### Xtend
   Contains all transformations: Model to text, text to Model, vignettes
   
### src
 Contains the JHISPTER app

## Deployment
* Jhipster authentification is: user/user
* Use of Heroku for deployment: https://www.heroku.com/

         yo jhipster:heroku