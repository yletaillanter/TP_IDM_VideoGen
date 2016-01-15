'use strict';

angular.module('jhipstervideogenApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


