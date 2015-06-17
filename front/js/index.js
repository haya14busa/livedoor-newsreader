import React from 'react'
import router from './router.js'

const rootElement = document.getElementById('react-app')

router.run((Handler, state) =>
  React.render(<Handler {...state} />, rootElement)
)
