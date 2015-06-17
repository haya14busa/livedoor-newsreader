import React from 'react'
import { RouteHandler } from 'react-router';
import CategoryList from './components/CategoryList.js'

export default class App {
  render() {
    return (
      <div>
        <CategoryList cgid="top"/>
        <RouteHandler {...this.props} />
      </div>
    )
  }
}
