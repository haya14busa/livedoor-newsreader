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

    const createRelatedArticle = function(article, relatedArticles) {
      if (relatedArticles.length > 0 && article) {
        return (
          <div>
            <hr />
            <div>
              <h5>{`「${article.title}」の関連記事`}</h5>
              <ul>{relatedArticles.slice(0, 3).map(createRelatedArticleSnippets)}</ul>
            </div>
          </div>
        )
      }
    }

    const image = function(article) {
      if (image in article) {
        return <img src={`${article.image}`} style={{float: 'right', margin: 20}} />
      }
    }

    return (
      <article>
        <h2><a href={`${this.state.article.link}`}>{this.state.article.title}</a></h2>
        {image(this.state.article)}
        {/*<div dangerouslySetInnerHTML={{__html: this.state.article.description}}></div>*/}
        {/*<hr />*/}
        <div dangerouslySetInnerHTML={{__html: this.state.article.html}}></div>
        {createRelatedArticle(this.state.article, this.state.relatedArticles)}
      </article>
    )
  }
}
