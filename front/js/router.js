import routes from './routes.js'
import {
  create as createRouter,
  HistoryLocation,
  HashLocation
} from 'react-router'

const location = HashLocation

export default createRouter({routes, location})
