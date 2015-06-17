import React, { PropTypes } from 'react'
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
        <h3 id={`guid-${article.guid}`} key={`article-${index}-${article.guid}`}>
          <a href={article.link}>{article.title}</a>
        </h3>
      )
    }
    if ('articles' in this.state.feed) {
      return <p>{this.state.feed.articles.map(createSnipets)}</p>
    } else return <p>Loading...</p>
  }
}

