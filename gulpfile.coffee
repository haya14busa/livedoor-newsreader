gulp = require 'gulp'
browserSync = require('browser-sync').create()
webpack = require 'webpack-stream'
plumber = require 'gulp-plumber'
rimraf = require 'rimraf'
less = require 'gulp-less'

config = require './gulp/config.coffee'

gulp.task 'build:js', ->
  gulp.src(config.webpack.entry)
    .pipe(plumber())
    .pipe(webpack(config.webpack))
    .pipe(gulp.dest(config.js.dest))

gulp.task 'build:less', ->
  gulp.src(config.less.src)
    .pipe(plumber())
    .pipe(less())
    .pipe(gulp.dest(config.less.dest))

gulp.task 'browser-sync', ->
  # ref: http://pauldijou.fr/blog/2014/08/05/browser-sync-play-framework/
  if browserSync.active
    return
  browserSync.init {
    # By default, Play is listening on port 9000
    proxy: 'localhost:9000',
    # We will set BrowserSync on the port 9001
    # TODO: better handling instad of hard-code
    port: 9001,
    # Reload all assets
    # Important: you need to specify the path on your source code
    # not the path on the url
    # files: [config.watch.js, config.watch.less, config.watch.html]
    files: [config.dest + '/**/*', config.watch.html]
  }

gulp.task 'clean', (cb) -> rimraf(config.dest, cb)

gulp.task 'build', ['build:js', 'build:less']

gulp.task 'dist', ['clean', 'build']

gulp.task 'watch', ['browser-sync'], ->
  gulp.watch config.watch.js, ['build:js']
  gulp.watch config.watch.less, ['build:less']
