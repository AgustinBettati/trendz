import React, { Component } from 'react'
import './App.css'
import { User } from '../types/types'
import {addUser, getAllUsers} from '../../api/UserApi'
import LoadingIndicator from '../common/LoadingIndicator'
import AppHeader from '../common/AppHeader'
import { Switch, Route } from 'react-router-dom'
import UserList from '../user-list/UserList'
import Alert from 'react-s-alert'
import Profile from "../Profile/Profile";
import ProfileEditor from "../ProfileEditor/ProfileEditor";


export type Props = {}

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
            <Route exact={true} path="/" render={(props) => <UserList {...props} users={users}/>}/>
            <Route exact={true} path="/profile" render={(props) => <Profile {...props} users={users}/>}/>
            <Route exact={true} path="/edit" render={(props) => <ProfileEditor {...props} users={users}/>}/>
          </Switch>
        </div>
        <Alert stack={{ limit: 3 }}
               timeout={3000}
               position='top-right' effect='slide' offset={65}/>
        <div className={'app-footer'}>
        </div>
      </div>
    )
  }
  handleLogout() {
    //TODO
  }
}

export default App
