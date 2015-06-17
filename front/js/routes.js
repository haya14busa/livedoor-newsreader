import React from 'react'
import { Route } from 'react-router'

import RssReader from './RSS.js'

export default (
  <Route name='rss' path='/' handler={RssReader}>
  </Route>
)
