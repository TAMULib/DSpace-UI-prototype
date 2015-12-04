DSpace UI Prototype
=============

This is a Spring and Angular.js based user interface to the DSpace repository platform. 

##Customization Capabilities

* How would someone change the colors, fonts, sizes of the site?
  
Sitewide sass variables are editable on an admin page.  These changes are applied at runtime and persisted in a file using the wro4j library.  We need to add a JPA entity for the theme to persist these values in a way that changes can be reflected on the admin page.

* How would someone modify the sitewide header/footer?
  
Currently, they are angular directives that could be replaced.  To enable runtime modification, one could code JPA entities that have some text, links, and images and associated forms/controllers to manipulate them.

* How would someone adjust the navigation bar to appear on left or right?

This is currently adjusted with a css attribute that could be controlled with sass and wro4j. This could also be persisted using JPA.

* How would someone change the location of the breadcrumb trail (e.g. from header to footer)?

The breadcrumb is a directive that could be moved in the angular template.  A sass/wro4j solution for controlling this at runtime may be possible.

* How would someone display additional metadata fields on the Item view page? For example, displaying “dc.publisher” on the default Item view site wide?

One would need a JPA entity to persist lists of fields at the item level. This could also be done at the collection or community level and inherited.  One would need forms/controllers to manage these at runtime.

* How would someone add new links or an entire section to the navigation menu (e.g. links to other university pages)?
  
One would need a JPA entity to persist the menu items in the navigation and forms/controllers to manage these at runtime.

##Modularization Capabilities

A new DSpace UI should allow for / support common modularization needs. Based on your prototype, please describe (via documentation) how you feel this UI platform may (or may not) be able to achieve the modularization examples below:

* For example, Embargo is an existing optional feature within DSpace. While it is disabled by default, many sites choose to enable it.

We imagine a scheme where code plugins (jars) are downloaded to a specified location that the webservice scans.  In the ui, these scanned jars could be enabled, tested, and configured in a module management interface a la drupal or wordpress.  On the webservice side, functionality could be augmented by the modules by means of Spring’s aspect oriented programming (AOP) functionality by iterating over the modules at appropriate join points.  

It’s less clear how this would be handled elegantly and universally on the angular frontend, but AOP style abstraction there could mitigate the need for custom templates for modules.  A library for AOP in angular is provided at https://github.com/mgechev/angular-aop .

*	Enabling Embargo functionality requires additional metadata fields to be captured in the deposit form (e.g. embargo date, embargo reason).

We expect that in a mature submission workflow feature the form contents would be generated on the backend from a JPA entity so that workflows would be modifiable at runtime.  This entity could be modified by AOP application of an Embargo module.

* Does this UI framework support the idea of easily enabling (or disabling) features that require UI-level changes? In other words, in this framework, do you feel enabling Embargo functionality could be automated via a configuration (e.g. embargo.enabled=true)? Or would it require manual editing of the deposit form to add additional metadata fields?

We envision that it would be modifiable at runtime by enabling or disabling a plugin.

* How could this UI platform support new extensions/add-ons?

There is an angular template for the item view with a itemTheater directive in the default display we have prototyped.  Another directive could be chosen conditionally in the item view based on item metadata or other criteria.

##Prototype Documentation

Each prototype MUST be submitted with the following documentation. Some of this documentation includes brainstorms on how you feel this prototype or UI platform might be extended to support more advanced UI features. Please base your answers on what you know (or have learned) about this UI platform during the prototyping process.

1. Describe the design of the prototype   (e.g. technologies/platforms used, including version of DSpace, etc.)?

The prototype is based on a reusable code base that we previously created to accelerate the development of internal enterprise apps.  This code base consists of two main components: 1) a webservice framework that extends Spring Boot to abstract away the use of HTTP versus websockets in the controllers and interfaces with our shibboleth authentication system.  2) An Angular.js frontend framework supplying some basic theming, messaging, and authentication functionality.  It was a natural fit for the prototype, as our apps typically communicate with external REST endpoints on the backend, and we appreciate the modularity afforded by the separation of webservice from UI and the ability to switch seamlessly between HTTP and websocket without rewriting webservice controllers.

For the purposes of this prototype, we did not create JPA entities on the webservice to mirror the DSOs we would be getting from DSpace REST, although this could be done whenever the need arises to persist custom settings on these objects.  The quickest route to a prototype was to pass through the REST responses from DSpace and have these parsed by the Angular frontend.  We used a single controller, IRController,  to support these calls as well as support the additional logic of creating a breadcrumb trail.

On the Angular side, there are models and controllers for each DSO in the hierarchy and for aggregations of them that need to be provided at each level (i.e. an Items controller for use in the Collection view as well as an Item controller for its own view).

2. How do you install the prototype on a new system? (Note: we will be testing the installation of prototypes in order to evaluate the installation of the platform itself)

A simple development deployment is achievable by cloning the repository from github and running mvn spring-boot:run in the directory of the pom.  One needs to provide a postgres database and an admin account in the target DSpace repository as configured in the src/main/resources/config/application.properties

3. How would you envision i18n (internationalization) support could be added to this UI prototype/platform in the future?

This has been done with angular modules, for example https://github.com/angular-translate/angular-translate .

4. How would you envision theming capabilities could be added to this UI prototype/platform in the future? In other words, how might local or third-party themes be installed or managed? Think of a theme as a collection of styles, fonts, logo, and page overrides.
 
At the simplest level, the views/templates, static resources, and sass on the Angular UI can be customized.  We have also demonstrated runtime theme adjustments with wro4j.

5. How would you envision supporting common DSpace authentication mechanisms (e.g. LDAP, Shibboleth) in this UI prototype/platform in the future?

The prototype currently supports login functionality using an embedded mock-shibboleth service provider that could be easily swapped out for the real thing.  It wouldn’t be hard to add basic password authentication to the prototype webservice as well.

However, authenticating to our prototype UI and webservice is not authentication to the underlying DSpace repository per se.  Further development in this direction involves some major decisions.  

The prototype currently authenticates itself using a single administrative eperson account and the simple password auth supported by DSpace’s REST API.  

If we wanted users to authenticate with their own usernames and passwords against the REST API, we would need the webservice to track and refresh their personal tokens and send the token with each request the REST API.  This would require some robust error reporting, as the user would not know about the inability to do certain things until it was tried – e.g. trying to create an item in a collection where they don’t have permission.  Further development to the REST API could enable collection of information about a user’s authorizations.  However, a separate and elaborate token broker would be required to allow users to authenticate with DSpace’s built-in shibboleth or ldap schemes to use the REST API.

On the other hand, responsibility for authorization policies could be offloaded to the webservice.  This would require a lot of coding and duplicate much of the work already in core DSpace, but may ultimately prove simpler.

