import React from 'react'
import { Link } from 'react-router'
import store from '../store.js'

export default class Article extends React.Component {
  constructor() {
    super()
    this.state = {
      article: {},
      relatedArticles: []
    }
  }

  update(guid) {
    store.findArticle(guid).then(article => {
      this.setState({article: article})
    })

    store.findRelatedArticle(guid).then(relatedArticles => {
      this.setState({relatedArticles: relatedArticles})
    })
  }

  componentWillReceiveProps(nextprops) {
    this.update(nextprops.params.guid)
  }

  componentDidMount() {
    this.update(Number(this.props.params.guid))
  }

  render() {
    const createRelatedArticleSnippets = function(article, index) {
      return (
        <li id={`related-guid-${article.guid}`} key={`related-${index}-${article.guid}-`}>
          <Link to='article' params={{guid: article.guid}}>{article.title}</Link>
        </li>
      )
    }

    const createRelatedArticle = function(articles) {
      if (articles.length > 0) {
        return (
          <div>
            <hr />
            <div>
              <h5>関連記事</h5>
              <ul>{articles.slice(0, 3).map(createRelatedArticleSnippets)}</ul>
            </div>
          </div>
        )
      }
    }

    return (
      <article>
        <h2>{this.state.article.title}</h2>
        <div dangerouslySetInnerHTML={{__html: this.state.article.description}}></div>
        <hr />
        <div dangerouslySetInnerHTML={{__html: this.state.article.html}}></div>
        {createRelatedArticle(this.state.relatedArticles)}
      </article>
    )
  }
}
