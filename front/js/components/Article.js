import React from 'react'
import store from '../store.js'

export default class Article extends React.Component {
  constructor() {
    super()
    this.state = {
      article: {}
    }
  }

  update(guid) {
    store.findArticle(guid).then(article => {
      this.setState({article: article})
    })
  }

  componentDidMount() {
    this.update(Number(this.props.params.guid))
  }

  render() {
    return (
      <article>
        <h2>{this.state.article.title}</h2>
        <div dangerouslySetInnerHTML={{__html: this.state.article.description}}></div>
        <hr />
        <div dangerouslySetInnerHTML={{__html: this.state.article.html}}></div>
      </article>
    )
  }
}
