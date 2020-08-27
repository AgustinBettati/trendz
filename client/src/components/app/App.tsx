import React, { Component } from 'react'
import './App.css'
import { Switch, Route } from 'react-router-dom'
import Register from "../Register/Register";
import PrivateRoute from "../Routing/PrivateRoute";
import {AppFrame} from "../Routing/AppFrame";
import Profile from "../Profile/Profile";
import ProfileEditor from "../ProfileEditor/ProfileEditor";

type MatchProps = {
  match: {
    url: string
  }
}

class App extends Component{
  render() {
    return (
      <div className="app">
        <Switch>
          <Route exact={true} path="/login" render={() => <div>login</div>}/>
          <Route exact={true} path="/register" component={Register}/>
          <PrivateRoute path={'/main'} component={({match}: MatchProps) => ([
              <AppFrame>
                <Switch>
                  <Route exact path={`${match.url}/profile`} component={Profile}/>
                  <Route exact path={`${match.url}/profileEditor`} component={ProfileEditor}/>
                </Switch>
              </AppFrame>
          ])}/>
        </Switch>
      </div>
    )
  }
}

export default App
