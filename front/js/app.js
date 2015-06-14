import React from 'react'
import { Router, Route, Link } from 'react-router'

class HelloWorld extends React.Component {
  render() {
    return <p>Hello, world!</p>
  }
}

React.render(
  <HelloWorld />,
  document.getElementById('react-app')
)
