eksctl create iamserviceaccount `
  --cluster menu-planner `
  --namespace kube-system `
  --name aws-load-balancer-controller `
  --role-name AmazonEKSLoadBalancerControllerRole `
  --attach-policy-arn arn:aws:iam::777605092423:policy/AWSLoadBalancerControllerIAMPolicy `
  --approve
