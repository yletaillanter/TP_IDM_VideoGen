'use strict';

angular.module('jhipstervideogenApp')
    .factory('Videogen', function ($resource) {
        return $resource('api/videogens/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
