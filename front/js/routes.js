import React from 'react'
import { Route } from 'react-router'

import Feeds from './components/Feeds.js'
import App from './app.js'

export default (
  <Route name='rss' path='/' handler={App}>
    <Route name='feed' path='feed/:cgid' handler={Feeds} />
  </Route>
)
