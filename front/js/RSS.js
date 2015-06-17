import React from 'react'
import store from './store.js'

class RssReader extends React.Component {
  render() {
    return (
      <div>
        <CategoryList />
        <Feeds cgid="top"/>
      </div>
    )
  }
}

class CategoryList extends React.Component {
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
      return <li key={`category-${index}-${category.cgid}`}>{category.name}</li>
    }
    return <p>{this.state.categories.map(createItem)}</p>
  }
}

class Feeds extends React.Component {
  constructor() {
    super() // XXX: 'this' is not allowed before super() ???
    this.state = {
      feed: {}
    }
  }

  componentDidMount() {
    store.fetchFeed(this.props.cgid).then(feed => {
      this.setState({feed: feed})
    })
  }

  render() {
    const createSnipets = function(article, index) {
      return (
        <div>
          <h3 id={`guid-${article.guid}`}>
            <a href={article.link}>{article.title}</a>
          </h3>
        </div>
      )
    }
    if ('articles' in this.state.feed) {
      return <p>{this.state.feed.articles.map(createSnipets)}</p>
    } else return <p>Loading...</p>
  }
}

export default RssReader
