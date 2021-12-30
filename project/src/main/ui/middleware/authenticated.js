export default function ({ $auth, redirect }) {
  if (!$auth.hasScope("ADMIN")) {
    console.log("You are not supposed to be here")
    return redirect('/')
  }
  console.log("You are supposed to be here")
}
