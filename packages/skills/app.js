'use strict';

/*
 * Defining the Package
 */
var Module = require('meanio').Module;

var Skills = new Module('skills');

/*
 * All MEAN packages require registration
 * Dependency injection is used to define required modules
 */
Skills.register(function(app, auth, database) {

    //We enable routing. By default the Package Object is passed to the routes
    Skills.routes(app, auth, database);

    //We are adding a link to the main menu for all authenticated users
    Skills.menus.add({
        title: 'skills example page',
        link: 'skills example page',
        roles: ['authenticated'],
        menu: 'main'
    });

    /**
    //Uncomment to use. Requires meanio@0.3.7 or above
    // Save settings with callback
    // Use this for saving data from administration pages
    Skills.settings({
        'someSetting': 'some value'
    }, function(err, settings) {
        //you now have the settings object
    });

    // Another save settings example this time with no callback
    // This writes over the last settings.
    Skills.settings({
        'anotherSettings': 'some value'
    });

    // Get settings. Retrieves latest saved settigns
    Skills.settings(function(err, settings) {
        //you now have the settings object
    });
    */

    return Skills;
});
