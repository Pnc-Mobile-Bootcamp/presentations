import type { Principal } from "./authz.ts";
import { accounts } from './authz.ts'
import { header } from "./utilities.ts";

// create our customer auth object
const customerA: Principal = {
    sub: "cust_A",
    scopes: ["accounts:read"],
    roles: ["customer"]
}

// a naive account handler
function getAccountNaive(p: Principal | undefined, id: string) {
    // verify we have a user principal
    if (!p) {
        return {
            status: 401,
            body: "authentication required"
        }
    }

    const acc = accounts[id]
    
    return acc ? { status: 200, body: acc } : { status: 404, body: "not found" }

}


const testIds = ["1", "2", "3", "99"]

// header('Testing a vulnerable API function')
// for (const id of testIds) {
//     console.log('---- loading account:', id)
//     console.log(getAccountNaive(customerA, id))
//     console.log()
// }

// we're going to be testing multiple versions of the function
// so let's make a test harness:
function testAccountFunction(title: string, fn: (p: Principal, id: string) => void ) {
    header(title)
    for (const id of testIds) {
        console.log('---- loading account:', id)
        console.log(fn(customerA, id))
        console.log()
    }
}

testAccountFunction('Testing a vulnerable API function', getAccountNaive)



// the first iteration let us see account that were not ours
// let's fix that
function getAccountBetter(p: Principal | undefined, id: string) {
    // verify we have a user principal
    if (!p) {
        return {
            status: 401,
            body: "authentication required"
        }
    }

    const acc = accounts[id]
    
    if (!acc) {
        return {
            status: 404,
            body: "not found"
        }
    }

    if (acc.ownerId === p.sub) {
        // the account is owned by the user
        return {
            status: 200,
            body: acc
        }
    }

    // the account exists, but is NOT owned by the user
    // HTTP standards say to return 403
    return {
        status: 403,
        body: "not allowed"
    }

}

testAccountFunction('Testing a better API function', getAccountBetter)


// accounts 2 and 3 return an auth error (not allowed)
// who benefits from that? (an attacker - they know the account exists)


function getAccountEvenBetter(p: Principal | undefined, id: string) {
    // verify we have a user principal
    if (!p) {
        return {
            status: 401,
            body: "authentication required"
        }
    }

    const acc = accounts[id]
    
    if (!acc) {
        return {
            status: 404,
            body: "not found"
        }
    }

    if (acc.ownerId === p.sub) {
        // the account is owned by the user
        return {
            status: 200,
            body: acc
        }
    }

    // the account exists, but is NOT owned by the user
    // HTTP standards say to return 403
    // but that benefits no-one but an attacker,
    // so return 404
    return {
        status: 404,
        body: "not found"
    }

}

testAccountFunction('Testing an even better API function', getAccountEvenBetter)


// suppose someone gives another person permission to view their account?
// (e.g. an accountant, power-of-attourney, another authorized account user)

const delegations: Record<string, string[]> = {
    'cust_A': ['cust_C']
}


function getAccountAlmostThere(p: Principal | undefined, id: string) {
    // verify we have a user principal
    if (!p) {
        return {
            status: 401,
            body: "authentication required"
        }
    }

    const acc = accounts[id]
    
    if (!acc) {
        return {
            status: 404,
            body: "not found"
        }
    }

    const isOwner = acc.ownerId === p.sub
    const isDelegate = (delegations[p.sub] ?? []).includes(acc.ownerId)
    if (isOwner || isDelegate) {
        // the account is owned by the user or they are a delegate for the owner
        return {
            status: 200,
            body: acc
        }
    }

    // the account exists, but is NOT owned by the user
    // HTTP standards say to return 403
    // but that benefits no-one but an attacker,
    // so return 404
    return {
        status: 404,
        body: "not found"
    }

}


testAccountFunction('Testing an almost best API function', getAccountAlmostThere)


// are we sending too much information?
// do they really need the entire account number?
// maybe that should require elevated permission

function getAccountFixed(p: Principal | undefined, id: string) {
    // verify we have a user principal
    if (!p) {
        return {
            status: 401,
            body: "authentication required"
        }
    }

    // check function-level permission
    if (!p.scopes.includes("accounts:read")) {
        return {
            status: 403,
            body: "not authorized"
        }
    }

    const acc = accounts[id]
    
    if (!acc) {
        return {
            status: 404,
            body: "not found"
        }
    }

    const isOwner = acc.ownerId === p.sub
    const isDelegate = (delegations[p.sub] ?? []).includes(acc.ownerId)
    if (isOwner || isDelegate) {
        // the account is owned by the user or they are a delegate for the owner
        // can the user see the full account number?
        const full = p.scopes.includes("accounts:full_number")
        return {
            status: 200,
            body: {
                ...acc,
                number: full ? acc.number : "****" + acc.number.slice(-4)
            }
        }
    }

    // the account exists, but is NOT owned by the user
    // HTTP standards say to return 403
    // but that benefits no-one but an attacker,
    // so return 404
    return {
        status: 404,
        body: "not found"
    }

}


testAccountFunction('Testing the fixed API function', getAccountFixed)

customerA.scopes = ["accounts:read", "accounts:full_number"]

testAccountFunction('Testing the elevated permission', getAccountFixed)


