#!/bin/bash

########
# Description: Generating helm values for multi versions of app in k8s test env.
# Maintainer: sa@7moor.com
# 具体描述: 由于测试环境存在同一应用多版本并行运行的情况，所以需要按不通的命名空间同时运行多版本，便于测试。
#   通话只有两个环境: dev和beta环境
########

set -e

if [[ $CI_COMMIT_REF_NAME =~ release-tonghua-beta ]]
then
    NAMESPACE='v7-abs'
    echo "beta环境命名空间为: $NAMESPACE"
    echo "$NAMESPACE" > namespace.txt
elif [[ $CI_COMMIT_REF_NAME =~ release-tonghua-dev ]]
then
    NAMESPACE='v7-abs-dev'
    echo "dev环境命名空间为: $NAMESPACE"
    echo "$NAMESPACE" > namespace.txt
else
    echo '非正式环境的触发CD的分支名称不符合规范!'
    echo '分支应该为: release-tonghua-(beta|dev)'
    echo 'CD阶段将退出'
    exit 1
fi
