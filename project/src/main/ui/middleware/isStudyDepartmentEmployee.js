export default function ({ $auth, redirect }) {
  if (!$auth.hasScope('STUDY_DEPARTMENT_EMPLOYEE')) {
    return redirect('/')
  }
}
