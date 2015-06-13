gulp = require 'gulp'
browserSync = require('browser-sync').create()

gulp.task 'browser-sync', ->
  # ref: http://pauldijou.fr/blog/2014/08/05/browser-sync-play-framework/
  browserSync.init {
    # By default, Play is listening on port 9000
    proxy: 'localhost:9000',
    # We will set BrowserSync on the port 9001
    # TODO: better handling instad of hard-code
    port: 9001,
    # Reload all assets
    # Important: you need to specify the path on your source code
    # not the path on the url
    files: ['public/stylesheets/**/*.css', 'public/javascripts/**/*.js', 'app/views/**/*.html'],
  }

gulp.task('watch', ['browser-sync'])
