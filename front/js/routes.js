import React from 'react'
import { Route } from 'react-router'

import Feeds from './components/Feeds.js'
import Article from './components/Article.js'
import App from './app.js'

export default (
  <Route name='rss' path='/' handler={App}>
    <Route name='feed' path='feed/:cgid' handler={Feeds} />
    <Route name='article' path='article/:guid' handler={Article} />
  </Route>
)
