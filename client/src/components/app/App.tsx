import React, { Component } from 'react'
import './App.css'
import { User } from '../types/types'
import { getAllUsers } from '../../api/UserApi'
import LoadingIndicator from '../common/LoadingIndicator'
import AppHeader from '../common/AppHeader/AppHeader'
import { Switch, Route } from 'react-router-dom'
import Profile from "../Profile/Profile";
import ProfileEditor from "../ProfileEditor/ProfileEditor";
import Register from "../Register/Register";

export type Props = {

}

type State = {
  users?: User[]
  loading: boolean
  authenticated: boolean
}

class App extends Component<Props, State> {

  constructor(props: Props) {
    super(props)
    this.state = {
      loading: true,
      authenticated: false
    }
  }

  componentDidMount() {
    getAllUsers().then(users => {
      this.setState({users: users, loading: false})
    })
  }

  handleLogout() {
    //TODO
  }

  render() {
    const {loading, users, authenticated} = this.state

    if (loading) {
      return <LoadingIndicator/>
    }

    return (
      <div className="app">
        <div className="app-top-box">
          <AppHeader authenticated={authenticated} onLogout={this.handleLogout}/>
        </div>
        <div className="app-body">
          <Switch>
            <Route exact={true} path="/profile" render={(props) => <Profile {...props} users={users}/>}/>
            <Route exact={true} path="/editProfile" render={(props) => <ProfileEditor {...props} users={users}/>}/>
            <Route exact={true} path="/register" render={(props) => <Register {...props}/>}/>
          </Switch>
        </div>
      </div>
    )
  }
}

export default App
