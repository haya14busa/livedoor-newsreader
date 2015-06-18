import React from 'react'
import { Link, RouteHandler } from 'react-router';
import CategoryList from './components/CategoryList.js'

export default class App {
  render() {
    return (
      <div>
        <h1 id='title'><Link to='rss'>livedoor news reader</Link></h1>
        <CategoryList cgid='top'/>
        <hr />
        <RouteHandler {...this.props} />
      </div>
    )
  }
}
