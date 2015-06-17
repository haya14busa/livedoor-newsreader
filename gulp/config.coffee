dest = './public/_target'
src = './front'

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
    js: js.src
    less: less.src
    html: 'app/views/**/*.html'

}
