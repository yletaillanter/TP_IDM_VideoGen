'use strict';

angular.module('jhipstervideogenApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
