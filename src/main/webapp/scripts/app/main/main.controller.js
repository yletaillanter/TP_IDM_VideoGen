'use strict';

angular.module('jhipstervideogenApp')
    .controller('VideogenController', function ($scope, Videogen, $http) {
        var jsPlaylistApi;

        $scope.normalMode = true;
        $scope.vignettes = [];
        $scope.selection = [];

        $scope.videogens = [];
        $scope.loadAll = function() {
            Videogen.query(function(result) {
               $scope.videogens = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Videogen.get({id: id}, function(result) {
                $scope.videogen = result;
                $('#saveVideogenModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.videogen.id != null) {
                Videogen.update($scope.videogen,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Videogen.save($scope.videogen,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Videogen.get({id: id}, function(result) {
                $scope.videogen = result;
                $('#deleteVideogenConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Videogen.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteVideogenConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveVideogenModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.videogen = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
        // Dev starts here
        $scope.generate = function () {
            $scope.normalMode = true;

            var loc = window.location.pathname;
            $http.get("/api/generateRandom").then(function (response) {
                var data = response.data.videos;
                var allVideos = [];
                data.forEach(function (video){
                    var newVideo = {sources:[{type:'video/mp4',src:'assets/videos/'+video.name}]};
                    allVideos.push(newVideo)
                })
                if(!jsPlaylistApi) {
                    jsPlaylistApi = flowplayer('.player', {
                        ratio: 9/16,
                        playlist: allVideos,
                        embed: {
                            playlist: true,
                            skin: "//releases.flowplayer.org/6.0.5/skin/playful.css"
                        },
                        splash: true,
                        rtmp: {
                            url: "rtmp://s3b78u0kbtx79q.cloudfront.net/cfx/st"
                        }
                    }); 
                }
                else {
                    jsPlaylistApi.setPlaylist(allVideos);
                }
                jsPlaylistApi.play(0);
               

                //console.log($("#flowplayer").html());
                //$("#player").append("<video><source type=\"video/mp4\" src=\"assets/videos/dramatic.mp4\"></video><div class=\"fp-playlist\"><a href=\"assets/videos/dramatic.mp4\"></a><a href=\"assets/videos/dog.mp4\"></a></div>");
            });

        };

        $scope.vignetteMode = function () {
            if(jsPlaylistApi) {
                jsPlaylistApi.pause();
            }
            $scope.normalMode = false;
            $scope.selection = [];
            $scope.vignettes = [];
            $http.get("/api/getVignettes").then(function (response) {
                var data = response.data.vignettes;
                var allVideos = [];
                data.forEach(function (vignette){
                    $scope.vignettes.push(vignette.name)
                })
            });
        };

        $scope.addToPlaylist = function (vignette) {
            $scope.selection.push(vignette.substring(0, vignette.length - 4));
            $("#"+vignette.substring(0, vignette.length - 8)).fadeTo( "slow" , 0.5, function() {
    // Animation complete.
        })
        }

        $scope.runSelection = function (vignette) {
            $scope.normalMode = true;
              var allVideos = [];
                $scope.selection.forEach(function (video){
                    var newVideo = {sources:[{type:'video/mp4',src:'assets/videos/'+video}]};
                    allVideos.push(newVideo)
                })
            if(!jsPlaylistApi) {
                    jsPlaylistApi = flowplayer('.player', {
                        ratio: 9/16,
                        playlist: allVideos,
                        embed: {
                            playlist: true,
                            skin: "//releases.flowplayer.org/6.0.5/skin/playful.css"
                        },
                        splash: true,
                        rtmp: {
                            url: "rtmp://s3b78u0kbtx79q.cloudfront.net/cfx/st"
                        }
                    }); 
                }
                else {
                    jsPlaylistApi.setPlaylist(allVideos);
                }
                jsPlaylistApi.play(0);
        }
    });
