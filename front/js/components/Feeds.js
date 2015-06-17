import React, { PropTypes } from 'react'
import { Link } from 'react-router'
import store from '../store.js'

// TODO: Remove store from this file

export default class Feeds extends React.Component {
  constructor() {
    super()
    this.state = {
      feed: {}
    }
  }

  updateFeed(cgid) {
    store.fetchFeed(cgid).then(feed => {
      this.setState({feed: feed})
    })
  }

  componentWillReceiveProps(nextprops) {
    this.updateFeed(nextprops.params.cgid)
  }

  componentDidMount() {
    this.updateFeed(this.props.params.cgid)
  }

  render() {
    const createSnipets = function(article, index) {
      return (
        <li id={`guid-${article.guid}`} key={`article-${index}-${article.guid}`}>
          <Link to='article' params={{guid: article.guid}}>{article.title}</Link>
        </li>
      )
    }
    if ('articles' in this.state.feed) {
      return (
        <div id="feeds-view">
          <ul>{this.state.feed.articles.map(createSnipets)}</ul>
        </div>
      )
    } else return <p>Loading...</p>
  }
}

