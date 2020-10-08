class Users extends React.Component {
    constructor(props) {
        super(props);
        this.state = {users: null};
        this.fetchUsers = this.fetchUsers.bind(this);
        this.fetchUsers();
    };

    fetchUsers() {
        window.fetch("/api/user/")
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Something went wrong ...');
                }
            })
            .then((users) => {
                this.setState({users: users});
            });
    }

    render() {
        return (
            <div>
                <UsersTable users={this.state.users}/>
                <UsersAddWidget handleOnAddUser={this.fetchUsers}/>
            </div>
        );
    };
}

class UsersTable extends React.Component {

    render() {
        const users = this.props.users;

        if (!users) {
            return <p>Ничего</p>;
        }

        let i = 0;

        const usersRendered = users
            .map((user) => {
                i++;
                let y = 0;
                let phonesString = "";
                for (let phone of user.phones) {
                    if (y === 0) {
                        phonesString += phone.number;
                    } else {
                        phonesString += "\n" + phone.number;
                    }
                    y++;
                }
                return (
                    <tr key={i.toString()}>
                        <th className="align-middle" scope="row">{i}</th>
                        <td className="align-middle">{user.name}</td>
                        <td className="align-middle">{user.login}</td>
                        <td className="align-middle">{user.address.street}</td>
                        <td className="align-middle new-line">{phonesString}</td>
                    </tr>
                );
            });

        return (
            <table className="table table-bordered table-striped mb-0">
                <thead className="thead-dark">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Name</th>
                    <th scope="col">Login</th>
                    <th scope="col">Address</th>
                    <th scope="col">Phones</th>
                </tr>
                </thead>
                <tbody>
                {usersRendered}
                </tbody>
            </table>
        );
    };
}

class UsersAddWidget extends React.Component {
    constructor(props) {
        super(props);

        let userInit =
            {
                "id": null,
                "name": "",
                "login": "",
                "password": "",
                "address":
                    {
                        "id": null,
                        "street": "",
                        "user": null
                    },
                "phones": [
                    {
                        "id": null,
                        "number": "",
                        "user": null
                    },
                    {
                        "id": null,
                        "number": "",
                        "user": null
                    }
                ]
            }

        this.state = {user: JSON.parse(JSON.stringify(userInit)), userInit: userInit, status: null, message: null};

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChangeLogin = this.handleChangeLogin.bind(this);
    }

    handleChangeName(e, user) {
        user.name = e.target.value;
        this.setState({user: user});
    }

    handleChangeLogin(e, user) {
        user.login = e.target.value;
        this.setState({user: user});
    }

    handleChangePassword(e, user) {
        user.password = e.target.value;
        this.setState({user: user});
    }

    handleChangeAddress(e, user) {
        user.address.street = e.target.value;
        this.setState({user: user});
    }

    handleChangePhone(e, user, key) {
        user.phones[key].number = e.target.value;
        this.setState({user: user});
    }

    handleSubmit(e) {
        const {user} = this.state;

        window.fetch("/api/user/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json;charset=UTF-8"
            },
            body: JSON.stringify(user)
        })
            .then(response => {
                if (response.ok) {
                    this.setState({
                        user: JSON.parse(JSON.stringify(this.state.userInit)),
                        message: "User successfully added!",
                        status: "ok"
                    });
                    this.props.handleOnAddUser();
                } else {
                    this.setState({
                        message: "Invalid data have passed for creation user. " +
                            "Please check that login is unique and try again", status: "error"
                    });
                }
            });
        e.preventDefault();
    }

    render() {
        const {user, message, status} = this.state;
        return (
            <div className="card">
                <div className="card-header bg-dark text-white">
                    <b>Add new user</b>
                </div>
                <div className="card-body">
                    {status === "error" &&
                    <div className="alert alert-warning mt-2" role="alert">
                        {message}
                    </div>
                    }
                    {status === "ok" &&
                    <div className="alert alert-success mt-2" role="alert">
                        {message}
                    </div>

                    }
                    <form onSubmit={this.handleSubmit}>
                        <div className="form-row">
                            <div className="form-group col-md-4">
                                <label><i className="fas fa-address-card"></i> Name</label>
                                <input type="text" className="form-control" id="name" value={user.name}
                                       onChange={event => this.handleChangeName(event, user)}
                                       placeholder="Name"/>
                            </div>
                            <div className="form-group col-md-4">
                                <label><i className="fas fa-sign-in-alt"></i> Login</label>
                                <input type="text" className="form-control" id="login" value={user.login}
                                       onChange={event => this.handleChangeLogin(event, user)}
                                       placeholder="Login"/>
                            </div>
                            <div className="form-group col-md-4">
                                <label><i className="fas fa-signature"></i> Password</label>
                                <input type="password" className="form-control" id="password" value={user.password}
                                       onChange={event => this.handleChangePassword(event, user)}
                                       placeholder="Password"/>
                            </div>
                        </div>
                        <div className="form-group">
                            <label><i className="fas fa-home"></i> Address</label>
                            <input type="text" className="form-control" id="address" value={user.address.street}
                                   onChange={event => this.handleChangeAddress(event, user)}
                                   placeholder="Address"/>
                        </div>
                        <div className="form-row">
                            <div className="form-group col-md-6">
                                <label><i className="fas fa-mobile"></i> Mobile phone</label>
                                <input type="text" className="form-control" id="phone1"
                                       value={user.phones[0].number}
                                       onChange={event => this.handleChangePhone(event, user, 0)}
                                       placeholder="Phone number"/>
                            </div>
                            <div className="form-group col-md-6">
                                <label><i className="fas fa-phone"></i> Home phone</label>
                                <input type="text" className="form-control" id="phone2"
                                       value={user.phones[1].number}
                                       onChange={event => this.handleChangePhone(event, user, 1)}
                                       placeholder="Phone number"/>
                            </div>
                        </div>
                        <button type="submit" className="btn btn-success btn-lg btn-block">Add</button>
                    </form>
                </div>
            </div>
        );
    }
}

const domContainer = document.querySelector('#root');
ReactDOM.render(<Users/>, domContainer);