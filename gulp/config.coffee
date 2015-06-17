path = require('path')

dest = './public/_target'
src = './front'
relativeSrcPath = path.relative('.', src)

js = {
  src: src + '/js/**/*.js'
  dest: dest + '/js'
}

less = {
  src: src + '/less/**/*.less'
  dest: dest + '/css'
}


module.exports = {
  src: src
  dest: dest

  js: js
  less: less

  webpack:
    entry: src + '/js/index.js'
    output:
      filename: 'bundle.js'
    resolve:
      extentions: ['', '.js']
    module: {
      loaders: [
        {
          test: /\.js$/
          exclude: /node_modules/
          loader: 'babel-loader'
          # loader: 'babel-loader?experimental&optional=selfContained'
        }
      ]
    }
    devtool: 'inline-source-map'
    # watch: true

  watch:
    js: relativeSrcPath + '/js/**'
    less: relativeSrcPath + '/less/**'
    html: 'app/views/**/*.html'

}
