helm install aws-load-balancer-controller eks/aws-load-balancer-controller `
-n kube-system `
--set clusterName=menu-planner `
--set serviceAccount.create=false `
--set serviceAccount.name=aws-load-balancer-controller