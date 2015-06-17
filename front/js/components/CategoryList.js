import React from 'react'
import { Link } from 'react-router'
import store from '../store.js'

export default class CategoryList extends React.Component {
  constructor() {
    super() // XXX: 'this' is not allowed before super() ???
    this.state = {
      categories: []
    }
  }

  componentDidMount() {
    store.fetchCategories().then(categories =>{
      this.setState({categories: categories})
    })
  }

  render() {
    const createItem = function(category, index) {
      return (
        <li key={`category-${index}-${category.cgid}`}>
          <Link to='feed' params={{ cgid: category.cgid }}>
            {category.name}
          </Link>
        </li>
      )
    }
    return (
      <nav id='category-navbar'>
        <ul>{this.state.categories.map(createItem)}</ul>
      </nav>
    )
  }
}


