(window.webpackJsonp=window.webpackJsonp||[]).push([[0],{"8+s/":function(e,t,n){"use strict";var A,r=n("q1tI"),u=(A=r)&&"object"==typeof A&&"default"in A?A.default:A;function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}var i=!("undefined"==typeof window||!window.document||!window.document.createElement);e.exports=function(e,t,n){if("function"!=typeof e)throw new Error("Expected reducePropsToState to be a function.");if("function"!=typeof t)throw new Error("Expected handleStateChangeOnClient to be a function.");if(void 0!==n&&"function"!=typeof n)throw new Error("Expected mapStateOnServer to either be undefined or a function.");return function(A){if("function"!=typeof A)throw new Error("Expected WrappedComponent to be a React component.");var c,l=[];function o(){c=e(l.map((function(e){return e.props}))),s.canUseDOM?t(c):n&&(c=n(c))}var s=function(e){var t,n;function r(){return e.apply(this,arguments)||this}n=e,(t=r).prototype=Object.create(n.prototype),t.prototype.constructor=t,t.__proto__=n,r.peek=function(){return c},r.rewind=function(){if(r.canUseDOM)throw new Error("You may only call rewind() on the server. Call peek() to read the current state.");var e=c;return c=void 0,l=[],e};var a=r.prototype;return a.UNSAFE_componentWillMount=function(){l.push(this),o()},a.componentDidUpdate=function(){o()},a.componentWillUnmount=function(){var e=l.indexOf(this);l.splice(e,1),o()},a.render=function(){return u.createElement(A,this.props)},r}(r.PureComponent);return a(s,"displayName","SideEffect("+function(e){return e.displayName||e.name||"Component"}(A)+")"),a(s,"canUseDOM",i),s}}},Fq5E:function(e,t,n){e.exports=n.p+"static/epfl-bbp-portrait-b7a35fd8bf457ca510a5e4560ae1ce1d.png"},K1hb:function(e,t){e.exports="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgdmlld0JveD0iMCAwIDYwIDYwIj4KICA8cGF0aCBpZD0iSWNvbl9hd2Vzb21lLWdpdGh1Yi1zcXVhcmUiIGRhdGEtbmFtZT0iSWNvbiBhd2Vzb21lLWdpdGh1Yi1zcXVhcmUiIGQ9Ik01My41NzEsMi4yNUg2LjQyOUE2LjQzLDYuNDMsMCwwLDAsMCw4LjY3OVY1NS44MjFBNi40Myw2LjQzLDAsMCwwLDYuNDI5LDYyLjI1SDUzLjU3MUE2LjQzLDYuNDMsMCwwLDAsNjAsNTUuODIxVjguNjc5QTYuNDMsNi40MywwLDAsMCw1My41NzEsMi4yNVpNMzcuMTM4LDUzLjYzOGMtMS4xMjUuMi0xLjU0LS41LTEuNTQtMS4wNzEsMC0uNzIzLjAyNy00LjQyLjAyNy03LjQwNmE1LjIxOCw1LjIxOCwwLDAsMC0xLjUxMy00LjExMmM0Ljk1NS0uNTQ5LDEwLjE3OS0xLjIzMiwxMC4xNzktOS43OSwwLTIuNDM4LS44NzEtMy42NTYtMi4yOS01LjIyM2E4LjM3Niw4LjM3NiwwLDAsMC0uMjI4LTYuMDI3Yy0xLjg2Mi0uNTc2LTYuMTIxLDIuNC02LjEyMSwyLjRhMjEuMDY5LDIxLjA2OSwwLDAsMC0xMS4xNDMsMHMtNC4yNTktMi45NzMtNi4xMjEtMi40YTguMzExLDguMzExLDAsMCwwLS4yMjgsNi4wMjcsNi43MjYsNi43MjYsMCwwLDAtMi4wODksNS4yMjNjMCw4LjUxOCw1LDkuMjQxLDkuOTUxLDkuNzlhNC44NTQsNC44NTQsMCwwLDAtMS40MiwyLjk4N2MtMS4yNzIuNTc2LTQuNTI3LDEuNTY3LTYuNDY5LTEuODYyYTQuNjY2LDQuNjY2LDAsMCwwLTMuNDE1LTIuMjljLTIuMTctLjAyNy0uMTQ3LDEuMzY2LS4xNDcsMS4zNjYsMS40NDYuNjcsMi40NjQsMy4yNDEsMi40NjQsMy4yNDEsMS4zLDMuOTc4LDcuNTEzLDIuNjM4LDcuNTEzLDIuNjM4LDAsMS44NjIuMDI3LDQuODg4LjAyNyw1LjQzOCwwLC41NzYtLjQsMS4yNzItMS41NCwxLjA3MWEyMi4yMTksMjIuMjE5LDAsMCwxLTE1LjAyNy0yMS4yLDIxLjI1NCwyMS4yNTQsMCwwLDEsMjEuNy0yMS42MjljMTIuMjk1LDAsMjIuMjU5LDkuMzM1LDIyLjI1OSwyMS42MjlBMjIuMDE1LDIyLjAxNSwwLDAsMSwzNy4xMzgsNTMuNjM4Wk0yNCw0NS40NTVjLS4yNTQuMDU0LS41LS4wNTQtLjUyMi0uMjI4LS4wMjctLjIuMTQ3LS4zNzUuNC0uNDI5LjI1NC0uMDI3LjUuMDguNTIyLjI1NFMyNC4yNjgsNDUuNCwyNCw0NS40NTVabS0xLjI3Mi0uMTIxYzAsLjE3NC0uMi4zMjEtLjQ2OS4zMjEtLjI5NS4wMjctLjUtLjEyMS0uNS0uMzIxLDAtLjE3NC4yLS4zMjEuNDY5LS4zMjFDMjIuNDg3LDQ0Ljk4NywyMi43MjgsNDUuMTM0LDIyLjcyOCw0NS4zMzVabS0xLjgzNS0uMTQ3Yy0uMDU0LjE3NC0uMzIxLjI1NC0uNTQ5LjE3NC0uMjU0LS4wNTQtLjQyOS0uMjU0LS4zNzUtLjQyOXMuMzIxLS4yNTQuNTQ5LS4yQzIwLjc4Niw0NC44MTIsMjAuOTYsNDUuMDEzLDIwLjg5Myw0NS4xODhabS0xLjY0Ny0uNzIzYy0uMTIxLjE0Ny0uMzc1LjEyMS0uNTc2LS4wOC0uMi0uMTc0LS4yNTQtLjQyOS0uMTIxLS41NDkuMTIxLS4xNDcuMzc1LS4xMjEuNTc2LjA4QzE5LjMsNDQuMDg5LDE5LjM2Niw0NC4zNTcsMTkuMjQ2LDQ0LjQ2NFptLTEuMjE5LTEuMjE5Yy0uMTIxLjA4LS4zNDgsMC0uNS0uMnMtLjE0Ny0uNDI5LDAtLjUyMmMuMTQ3LS4xMjEuMzc1LS4wMjcuNS4xNzRhLjM5Mi4zOTIsMCwwLDEsMCwuNTQ5Wm0tLjg3MS0xLjNjLS4xMjEuMTIxLS4zMjEuMDU0LS40NjktLjA4LS4xNDctLjE3NC0uMTc0LS4zNzUtLjA1NC0uNDY5LjEyMS0uMTIxLjMyMS0uMDU0LjQ2OS4wOEMxNy4yNSw0MS42NTIsMTcuMjc3LDQxLjg1MywxNy4xNTYsNDEuOTQ2Wm0tLjktLjk5MWEuMjUyLjI1MiwwLDAsMS0uMzc1LjA1NGMtLjE3NC0uMDgtLjI1NC0uMjI4LS4yLS4zNDhhLjMxNS4zMTUsMCwwLDEsLjM3NS0uMDU0QzE2LjIzMiw0MC43LDE2LjMxMiw0MC44NDgsMTYuMjU5LDQwLjk1NVoiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDAgLTIuMjUpIiBmaWxsPSIjZmZmIi8+Cjwvc3ZnPgo="},ML3p:function(e,t){e.exports="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgdmlld0JveD0iMCAwIDYwIDYwIj4KICA8cGF0aCBpZD0iSWNvbl9hd2Vzb21lLXR3aXR0ZXItc3F1YXJlIiBkYXRhLW5hbWU9Ikljb24gYXdlc29tZS10d2l0dGVyLXNxdWFyZSIgZD0iTTUzLjU3MSwyLjI1SDYuNDI5QTYuNDMsNi40MywwLDAsMCwwLDguNjc5VjU1LjgyMUE2LjQzLDYuNDMsMCwwLDAsNi40MjksNjIuMjVINTMuNTcxQTYuNDMsNi40MywwLDAsMCw2MCw1NS44MjFWOC42NzlBNi40Myw2LjQzLDAsMCwwLDUzLjU3MSwyLjI1Wk00Ny4wMjIsMjMuNTE4Yy4wMjcuMzc1LjAyNy43NjMuMDI3LDEuMTM4LDAsMTEuNjEyLTguODM5LDI0Ljk5MS0yNC45OTEsMjQuOTkxQTI0Ljg3NiwyNC44NzYsMCwwLDEsOC41NzEsNDUuNzFhMTguNSwxOC41LDAsMCwwLDIuMTE2LjEwNywxNy42MTEsMTcuNjExLDAsMCwwLDEwLjktMy43NSw4LjgsOC44LDAsMCwxLTguMjEtNi4wOTQsOS40NjcsOS40NjcsMCwwLDAsMy45NjQtLjE2MSw4Ljc4NCw4Ljc4NCwwLDAsMS03LjAzMS04LjYyNVYyNy4wOGE4Ljc3OCw4Ljc3OCwwLDAsMCwzLjk2NCwxLjExMiw4Ljc2NSw4Ljc2NSwwLDAsMS0zLjkxMS03LjMxMyw4LjY3NCw4LjY3NCwwLDAsMSwxLjE5Mi00LjQzMywyNC45NDEsMjQuOTQxLDAsMCwwLDE4LjEwNyw5LjE4OCw4LjgsOC44LDAsMCwxLDE0Ljk4Ny04LjAyMkExNy4yMTIsMTcuMjEyLDAsMCwwLDUwLjIyMywxNS41YTguNzYxLDguNzYxLDAsMCwxLTMuODU3LDQuODM1LDE3LjQ4LDE3LjQ4LDAsMCwwLDUuMDYyLTEuMzY2QTE4LjQ4OSwxOC40ODksMCwwLDEsNDcuMDIyLDIzLjUxOFoiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDAgLTIuMjUpIiBmaWxsPSIjZmZmIi8+Cjwvc3ZnPgo="},"NAU+":function(e,t){e.exports="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMCIgaGVpZ2h0PSIyMSIgdmlld0JveD0iMCAwIDMwIDIxIiBmaWxsPSIjMDAwIj4KICA8ZyBpZD0iSWNvbl9mZWF0aGVyLW1lbnUiIGRhdGEtbmFtZT0iSWNvbiBmZWF0aGVyLW1lbnUiIHRyYW5zZm9ybT0idHJhbnNsYXRlKC0zIC03LjUpIj4KICAgIDxwYXRoIGlkPSJQYXRoXzciIGRhdGEtbmFtZT0iUGF0aCA3IiBkPSJNNC41LDE4aDI3IiBmaWxsPSJub25lIiBzdHJva2U9InJnYmEoMjAwLDIwMCwyMDApIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIvPgogICAgPHBhdGggaWQ9IlBhdGhfOCIgZGF0YS1uYW1lPSJQYXRoIDgiIGQ9Ik00LjUsOWgyNyIgZmlsbD0ibm9uZSIgc3Ryb2tlPSJyZ2JhKDIwMCwyMDAsMjAwKSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIiBzdHJva2Utd2lkdGg9IjMiLz4KICAgIDxwYXRoIGlkPSJQYXRoXzkiIGRhdGEtbmFtZT0iUGF0aCA5IiBkPSJNNC41LDI3aDI3IiBmaWxsPSJub25lIiBzdHJva2U9InJnYmEoMjAwLDIwMCwyMDApIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiIHN0cm9rZS13aWR0aD0iMyIvPgogIDwvZz4KPC9zdmc+Cg=="},Oezq:function(e,t){e.exports="data:image/vnd.microsoft.icon;base64,AAABAAEAICAAAAEAIACoEAAAFgAAACgAAAAgAAAAQAAAAAEAIAAAAAAAABAAABILAAASCwAAAAAAAAAAAAAAAAAA9MdEAPTHRAT0x0RX9MdEe/THRHn0x0R59MdEefXHRHnqv0F5n38tMM+oOgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAO6tAADurQA07q0Aee6tAHnurQB57q0Aee6tAHnurQB74J4IWK9oIQTAehgAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9chE/+i9Qf9vVyCkAAAACyobEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADurQAA7q0ACu6tAKrurQD/7q0A/+6tAP/urQD/7q0A/+upAv/QjBC7qWElCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/1yET/6r5B/1hCGvgnFw5bIxQJAEozIQAAAAAAAAAAAAAAAAAAAAAA7q0AAO6tAADurQBg7q0A9+6tAP/urQD/7q0A/+6tAP/vrgD/2ZYL/7x2Gru4chwJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//XIRP/qv0H/WkMa/zchEspRNBwhTDAaAAAAAAAAAAAAAAAAAAAAAADurQAA7q0AH+6tAMrurQD/7q0A/+6tAP/urQD/7q0A/+elBP/Efxb/unQbu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9cdE/+u/Qv9fRhz/QigV/147H4P/xn8BflQxAAAAAAAAAAAA7q0AAO6tAAHurQCI7q0A/+6tAP/urQD/7q0A/+6tAP/urQD/0Y0Q/7p0G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/1x0T/679C/2RJHv9MLhj/bkQj53xPKjx6TCcAAAAAAAAAAADurQAA7q0AP+6tAOXurQD/7q0A/+6tAP/urQD/764A/+CeCP+/eRn/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//XHRP/rv0L/a00f/1c1G/91SCT/eUwnqH9TMA19US0A7q0AAO6tAAzurQCu7q0A/+6tAP/urQD/7q0A/+6tAP/rqgL/yYUT/7p0G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9cdE/+vAQv9yUiL/Yzwf/3hKJf94SiX5ek0oX3ZIIQDtrAEA7q0AY+6tAPjurQD/7q0A/+6tAP/urQD/764A/9iVC/+8dhv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/7MBC/3pXJP9sQiL/eEol/3hKJf95SybNdkotIfWzAB/urQDN7q0A/+6tAP/urQD/7q0A/+6uAP/mpAT/w34X/7p0G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/swEL/hF0n/3NGI/94SiX/eEol/3hKJf95TCeI7awBju6tAP/urQD/7q0A/+6tAP/urQD/7q0A/9CMEP+6dBv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9MdE/+3AQv+NYir/dkgk/3hKJf94SiX/d0kl/5hlHPPpqQLy7q0A/+6tAP/urQD/7q0A/++uAP/fnQj/vnkZ/7t1G/+7dRv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/7sFC/5NmLP52SCX/eEol/3hKJf9+TyP/yo8L/++uAP/urQD/7q0A/+6tAP/urQD/66oC/smEFP66dBv/u3Ub/7t1G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/zxkT1nW8vz3ZIJf54SiX/dkkl/6dxFv/sqwH/7q0A/+6tAP/urQD/7q0A/++uAP7gngfFvHYb87t1G/+7dRv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9MdE//XIRPHDljpmd0km0ndJJf+IVyD/2JsH/++uAP/urQD/7q0A/+6tAP/urQD/764A0dCMEGG6dBvvu3Ub/7t1G/+7dRv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/9MdE8/jLRTt1SCdseUsl+7qCEP/vrgD/7q0A/+6tAP/urQD/7q0A/+6tAPvwrwBiuXIcNrt1G/G7dRv/u3Ub/7t1G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/0x0Tz9slEOz8YMg2kbxm746QE/++uAP/urQD/7q0A/+6tAP/urQD/7q0As//CAAy6dBw3u3Ub8bt1G/+7dRv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9MdE//THRPP0x0U78agACu2sAbburQD/7q0A/+6tAP/urQD/764A/9+hAv9/WQ2wAAAkDL12Gze7dRvxu3Ub/7t1G/+7dRv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/9MdE8/TIRjrurABm7q0A+u6tAP/urQD/7q0A/+6tAP/wrgD/nG4I/0EoFPtGLBpjwXkcNrt1G/G7dRv/u3Ub/7t1G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/0x0Xx8r0qYu6tAM7urQD/7q0A/+6tAP/urQD/8K4A/82UA/9XORH/RSoW/1EzG8+WXx5fvHYb77t1G/+7dRv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9MdE//THRPTwtRTM7q0A/u6tAP/urQD/7q0A/+6tAP/rqwD/gFkM/0EoFf9PMRn/WTcc/X5PH8K7dRvzu3Ub/7t1G/+7dRv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/9MZB/u+zEP7urQD/7q0A/+6tAP/urQD/8K4A/7SBBv9KLxP/Sy4Y/1c2G/9hPB7/ekwf/rVxG/67dRv/u3Ub/7t1G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/0xkH/77MQ/+6tAP/urQD/7q0A/+6tAP/oqQHybksP8kYqF/9UNBr/Xzsd/2hAIP9+TiH/tHEb/7t1G/+7dRv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9MdE//TGQf/vsxD/7q0A/+6tAP/urQD/7q0A/+yrAYtMMBmRUDEZ/1w5HP9mPx//bkMi/4NRIv+2chv/u3Ub/7t1G/+7dRv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/9MZB/++zEP/urQD/7q0A/+6tAP/urQDP9rQAIlE0IyNbOB3RYz0f/2xDIf9yRiT/hVMi/7dzHP+7dRv/u3Ub/7t1G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/0xkH/77MQ/+6tAP/urQD/7q0A+e6tAGDtrAEAZTwaAGZAImpqQSH5cUYj/3VIJP+HUyP/t3Mc/7t1G/+7dRv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9MdE//TGQf/vsxD/7q0A/+6tAP/urQCr7q0ADe6tAABySioAc0ssDXFGJLN0SCT/d0kl/4dTI/+3cxz/u3Ub/7t1G/+7dRv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/9MZB/++zEP/urQD/7q0A6e6tAD/urQAAAAAAAAAAAAB2SSUAd0woRHdKJeh3SSX/h1Mj/7dzHP+7dRv/u3Ub/7t1G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/0xkH/77MQ/+6tAP/urQCF7q0AAe6tAAAAAAAAAAAAAIBVMwCOaEsBeUwnjHdKJf+HUyP/t3Mc/7t1G/+7dRv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdECPTHRLn0x0T/9MdE//THRP/0x0T/9MdE//TGQf/vsxD/7q0AzO6tACLurQAAAAAAAAAAAAAAAAAAAAAAAHtNKQB8UCwieEomz4dTI/+3cxz/u3Ub/7t1G/+7dRv/u3Ub/7t1G/+7dRu7u3UbCbt1GwAAAAAAAAAAAPTHRAD0x0QI9MdEufTHRP/0x0T/9MdE//THRP/0x0T/9MZB/++zEfjurABd7q0AAO6tAAAAAAAAAAAAAAAAAAAAAAAAhl07AHVHIQB4Sylnh1Qj+LdzHP+7dRv/u3Ub/7t1G/+7dRv/u3Ub/7t1G7u7dRsJu3UbAAAAAAAAAAAA9MdEAPTHRAj0x0S59MdE//THRP/0x0T/9MdE//THRP/0xkD/8LYZp+ylAAzurQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAflIvAG1INQyPWSSwt3Ic/7t1G/+7dRv/u3Ub/7t1G/+7dRv/u3Ubu7t1Gwm7dRsAAAAAAAAAAAD0x0QA9MdEBfTHRHL0x0Sg9MdEnvTHRJ70x0Se9MdEnvTGQZ3yviw688M6AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAuHIaAKBmJEC4cxydu3Ubnrt1G567dRueu3Ubnrt1G6C7dRt0u3UcBbt1HAAAAAAAwB/4A8AP8APAD/ADwAfgA8ADwAPAA8ADwAGAA8ABgAPAAAADwAAAA8AAAAPAAAADwAAAA8AAAAPAAAADwAAAA8AAAAPAAAADwAAAA8AAAAPAAAADwAAAA8AAAAPAAAADwAGAA8ABgAPAA8ADwAPAA8AH4APAD/ADwA/wA8Af+AM="},UKwi:function(e,t){e.exports="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgdmlld0JveD0iMCAwIDYwIDYwIj4KICA8cGF0aCBpZD0iSWNvbl9hd2Vzb21lLWxpbmtlZGluIiBkYXRhLW5hbWU9Ikljb24gYXdlc29tZS1saW5rZWRpbiIgZD0iTTU1LjcxNCwyLjI1SDQuMjcyQTQuMyw0LjMsMCwwLDAsMCw2LjU3NlY1Ny45MjRBNC4zLDQuMywwLDAsMCw0LjI3Miw2Mi4yNUg1NS43MTRBNC4zMTYsNC4zMTYsMCwwLDAsNjAsNTcuOTI0VjYuNTc2QTQuMzE2LDQuMzE2LDAsMCwwLDU1LjcxNCwyLjI1Wk0xOC4xMzQsNTMuNjc5SDkuMjQxVjI1LjA0NWg4LjkwNlY1My42NzlaTTEzLjY4OCwyMS4xMzRhNS4xNTYsNS4xNTYsMCwxLDEsNS4xNTYtNS4xNTYsNS4xNTgsNS4xNTgsMCwwLDEtNS4xNTYsNS4xNTZaTTUxLjQ2OSw1My42NzlINDIuNTc2VjM5Ljc1YzAtMy4zMjEtLjA2Ny03LjU5NC00LjYyMS03LjU5NC00LjYzNCwwLTUuMzQ0LDMuNjE2LTUuMzQ0LDcuMzUzdjE0LjE3SDIzLjcxOVYyNS4wNDVIMzIuMjV2My45MTFoLjEyMWE5LjM2Nyw5LjM2NywwLDAsMSw4LjQyNC00LjYyMWM5LDAsMTAuNjc0LDUuOTMzLDEwLjY3NCwxMy42NDdaIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgwIC0yLjI1KSIgZmlsbD0iI2ZmZiIvPgo8L3N2Zz4K"},ZhWT:function(e,t){var n="undefined"!=typeof Element,A="function"==typeof Map,r="function"==typeof Set,u="function"==typeof ArrayBuffer&&!!ArrayBuffer.isView;e.exports=function(e,t){try{return function e(t,a){if(t===a)return!0;if(t&&a&&"object"==typeof t&&"object"==typeof a){if(t.constructor!==a.constructor)return!1;var i,c,l,o;if(Array.isArray(t)){if((i=t.length)!=a.length)return!1;for(c=i;0!=c--;)if(!e(t[c],a[c]))return!1;return!0}if(A&&t instanceof Map&&a instanceof Map){if(t.size!==a.size)return!1;for(o=t.entries();!(c=o.next()).done;)if(!a.has(c.value[0]))return!1;for(o=t.entries();!(c=o.next()).done;)if(!e(c.value[1],a.get(c.value[0])))return!1;return!0}if(r&&t instanceof Set&&a instanceof Set){if(t.size!==a.size)return!1;for(o=t.entries();!(c=o.next()).done;)if(!a.has(c.value[0]))return!1;return!0}if(u&&ArrayBuffer.isView(t)&&ArrayBuffer.isView(a)){if((i=t.length)!=a.length)return!1;for(c=i;0!=c--;)if(t[c]!==a[c])return!1;return!0}if(t.constructor===RegExp)return t.source===a.source&&t.flags===a.flags;if(t.valueOf!==Object.prototype.valueOf)return t.valueOf()===a.valueOf();if(t.toString!==Object.prototype.toString)return t.toString()===a.toString();if((i=(l=Object.keys(t)).length)!==Object.keys(a).length)return!1;for(c=i;0!=c--;)if(!Object.prototype.hasOwnProperty.call(a,l[c]))return!1;if(n&&t instanceof Element)return!1;for(c=i;0!=c--;)if(("_owner"!==l[c]&&"__v"!==l[c]&&"__o"!==l[c]||!t.$$typeof)&&!e(t[l[c]],a[l[c]]))return!1;return!0}return t!=t&&a!=a}(e,t)}catch(a){if((a.message||"").match(/stack|recursion/i))return console.warn("react-fast-compare cannot handle circular refs"),!1;throw a}}},hAFY:function(e,t,n){"use strict";var A=n("q1tI"),r=n("Wbzz"),u=n("rbAC"),a=n.n(u),i=n("xfNg"),c=n.n(i),l=function(){return A.createElement("div",null,A.createElement("ul",{className:"submenu"},A.createElement("li",null,A.createElement(r.a,{to:"/products/nexus-fusion"},"Nexus Fusion")),A.createElement("li",null,A.createElement(r.a,{to:"/products/nexus-forge"},"Nexus Forge")),A.createElement("li",null,A.createElement(r.a,{to:"/products/nexus-delta"},"Nexus Delta"))))},o=function(){return A.createElement("div",null,A.createElement("ul",{className:"submenu"},A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/getting-started/understanding-knowledge-graphs.html"},"Understanding Knowledge Graphs")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/getting-started/try-nexus.html"},"Try Nexus")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/getting-started/running-nexus.html"},"Running Nexus"))))},s=function(){return A.createElement("div",{className:"dev-list"},A.createElement("ul",{className:"submenu"},A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs"},"Overview")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/releases"},"Releases")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/roadmap"},"Roadmap")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/faq"},"FAQ")),A.createElement("li",null,A.createElement("h4",{className:"submenu-title"},"By Product"),A.createElement("ul",{className:"submenu"},A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/fusion"},"Nexus Fusion")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/forge"},"Nexus Forge")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/delta"},"Nexus Delta")),A.createElement("li",null,A.createElement("a",{href:"https://bluebrainnexus.io/docs/utilities"},"Utilities"))))))},M=n("NAU+"),d=n.n(M),L=n("zv5M"),m=n.n(L),E=function(e){var t=e.children,n=e.title,r=e.defaultOpened,u=void 0!==r&&r,a=A.useState(u),i=a[0],c=a[1];return A.createElement(A.Fragment,null,A.createElement("button",{className:"menu-item-button",onClick:function(){c(!i)}},n),i&&A.createElement("div",{className:"submenu"},t))},T=function(){var e=A.useState(!1),t=e[0],n=e[1];return A.createElement("nav",{className:"mobile-menu"},A.createElement("button",{className:"button",onClick:function(){n(!0)}},A.createElement("img",{src:d.a,alt:"mobile menu open button"})),A.createElement("div",{className:"menu card"+(t?" opened":"")},A.createElement("button",{className:"close-button",onClick:function(){n(!1)}},A.createElement("img",{src:m.a,alt:"mobile menu close button"})),A.createElement("ul",null,A.createElement("li",{className:"menu-item"},A.createElement(E,{title:"Products",defaultOpened:!0},A.createElement(l,null))),A.createElement("li",{className:"menu-item"},A.createElement(E,{title:"Getting Started"},A.createElement(o,null))),A.createElement("li",{className:"menu-item"},A.createElement(E,{title:"Developers"},A.createElement(s,null))))))},w=function(e){var t=e.children,n=e.title,r=A.useState(!1),u=r[0],a=r[1],i=function(e){return function(){a(e||!u)}};return A.createElement("div",{className:"dropdown-group",onMouseLeave:i(!1)},A.createElement("button",{className:"nav-button",onClick:i(),onMouseEnter:i(!0)},n),A.createElement("div",{className:"dropdown card"+(u?" opened":"")},A.createElement("div",{className:"holder"}),A.createElement("div",{className:"arrow-up"}),t))},j=function(){return A.createElement("nav",{className:"dropdown-nav"},A.createElement(w,{title:"Products"},A.createElement(l,null)),A.createElement(w,{title:"Getting Started"},A.createElement(o,null)),A.createElement(w,{title:"Developers"},A.createElement(s,null)))};function N(){return A.createElement("header",{className:"header"},A.createElement("div",{className:"container"},A.createElement("div",{className:"content stretch"},A.createElement("a",{href:"https://www.epfl.ch/",target:"_blank"},A.createElement("div",{className:"epfl-logo"},A.createElement("img",{src:c.a,alt:"EPFL logo"}))),A.createElement(r.a,{to:"/"},A.createElement("div",{className:"logo"},A.createElement("img",{src:a.a,alt:"Nexus Logo"}),A.createElement("span",null,"Blue Brain Nexus"))),A.createElement(j,null),A.createElement(T,null))))}var y=function(){return A.createElement("div",null,A.createElement("ul",{className:"submenu"},A.createElement("li",null,A.createElement("a",{href:"https://go.epfl.ch/privacy-policy",target:"_blank"},"Privacy Policy")),A.createElement("li",null,A.createElement("a",{href:"https://www.epfl.ch/about/presidency/presidents-team/legal-affairs/epfl-privacy-policy/cookies-policy/",target:"_blank"},"Cookie Policy")),A.createElement("li",null,A.createElement("a",{href:"https://www.epfl.ch/about/overview/regulations-and-guidelines/disclaimer/",target:"_blank"},"Disclaimer"))))},f=n("Fq5E"),D=n.n(f),p=n("UKwi"),b=n.n(p),I=n("K1hb"),x=n.n(I),C=n("ML3p"),g=n.n(C);function h(){return A.createElement("footer",{className:"footer gradient"},A.createElement("div",{className:"container"},A.createElement("div",{className:"content"},A.createElement("div",{className:"columns"},A.createElement("div",{className:"column"},A.createElement("a",{href:"https://www.epfl.ch/research/domains/bluebrain/"},A.createElement("img",{className:"logo",src:D.a,alt:"Blue Brain Project Logo"}))),A.createElement("div",{className:"column"},A.createElement("h3",null,"Products"),A.createElement(l,null)),A.createElement("div",{className:"column"},A.createElement("h3",null,"Developers"),A.createElement(s,null)),A.createElement("div",{className:"column"},A.createElement("h3",null,"Terms"),A.createElement(y,null)),A.createElement("div",{className:"column"},A.createElement("h3",null,"Contact Us"),A.createElement("div",{className:"social-icons"},A.createElement("a",{href:"https://www.linkedin.com/showcase/blue-brain-project/"},A.createElement("img",{className:"logo",src:b.a,alt:"LinkedIn"})),A.createElement("a",{href:"https://github.com/BlueBrain/nexus/discussions"},A.createElement("img",{className:"logo",src:x.a,alt:"GitHub"})),A.createElement("a",{href:"https://twitter.com/bluebrainnexus"},A.createElement("img",{className:"logo",src:g.a,alt:"Twitter"}))),A.createElement("div",{className:"contacts"},A.createElement("p",null,"EPFL Blue Brain Project"),A.createElement("p",null,"Campus Biotech"),A.createElement("p",null,"Chemin des Mines 9"),A.createElement("p",null,"1202 Geneva"),A.createElement("p",null,A.createElement("a",{href:"tel:+41 21 69 37 660"},"+41 21 69 37 660"))))),A.createElement("div",{className:"legal"},A.createElement("p",null,"Blue Brain Nexus is Open Source and available under the Apache License 2.0."),A.createElement("p",null,"Blue Brain Project/EPFL 2005 – ",(new Date).getFullYear(),". All rights reserved.")))))}var S=n("qhky"),v=n("YwZP"),P=n("Oezq"),z=n.n(P),R=n("oqH1"),Q=n.n(R),U="26522286",H=function(e){var t=e.title,n=e.description,u=e.image,a=(Object(v.useLocation)().pathname,Object(r.c)(U).site.siteMetadata),i=a.defaultTitle,c=a.defaultDescription,l=(a.siteUrl,a.defaultImage),o=a.twitterUsername,s={title:t||i,description:n||c,image:""+(u||l),url:Q.a};return A.createElement(S.a,null,A.createElement("title",null,s.title),A.createElement("link",{rel:"icon",type:"image/png",href:z.a,sizes:"32x32"}),A.createElement("meta",{name:"description",content:s.description}),A.createElement("meta",{name:"image",content:s.image}),A.createElement("meta",{property:"og:type",content:"website"}),A.createElement("meta",{name:"viewport",content:"width=device-width, initial-scale=1.0"}),s.url&&A.createElement("meta",{property:"og:url",content:s.url}),s.title&&A.createElement("meta",{property:"og:title",content:s.title}),s.image&&A.createElement("meta",{name:"og:image",content:s.image}),s.description&&A.createElement("meta",{property:"og:description",content:s.description}),s.image&&A.createElement("meta",{property:"og:image",content:s.image}),A.createElement("meta",{name:"twitter:card",content:"summary_large_image"}),o&&A.createElement("meta",{name:"twitter:creator",content:o}),s.title&&A.createElement("meta",{name:"twitter:title",content:s.title}),s.description&&A.createElement("meta",{name:"twitter:description",content:s.description}),s.image&&A.createElement("meta",{name:"twitter:image",content:s.image}))};t.a=function(e){var t=e.children,n=function(e,t){if(null==e)return{};var n,A,r={},u=Object.keys(e);for(A=0;A<u.length;A++)n=u[A],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,["children"]);return A.createElement(A.Fragment,null,A.createElement(H,n),A.createElement(N,null),t,A.createElement(h,null))}},oqH1:function(e,t,n){e.exports=n.p+"static/slate-99c92fb7853f45240cfca8985160ef5a.jpeg"},qhky:function(e,t,n){"use strict";(function(e){n.d(t,"a",(function(){return me}));var A,r,u,a,i=n("17x9"),c=n.n(i),l=n("8+s/"),o=n.n(l),s=n("ZhWT"),M=n.n(s),d=n("q1tI"),L=n.n(d),m=n("6qGY"),E=n.n(m),T="bodyAttributes",w="htmlAttributes",j="titleAttributes",N={BASE:"base",BODY:"body",HEAD:"head",HTML:"html",LINK:"link",META:"meta",NOSCRIPT:"noscript",SCRIPT:"script",STYLE:"style",TITLE:"title"},y=(Object.keys(N).map((function(e){return N[e]})),"charset"),f="cssText",D="href",p="http-equiv",b="innerHTML",I="itemprop",x="name",C="property",g="rel",h="src",S="target",v={accesskey:"accessKey",charset:"charSet",class:"className",contenteditable:"contentEditable",contextmenu:"contextMenu","http-equiv":"httpEquiv",itemprop:"itemProp",tabindex:"tabIndex"},P="defaultTitle",z="defer",R="encodeSpecialCharacters",Q="onChangeClientState",U="titleTemplate",H=Object.keys(v).reduce((function(e,t){return e[v[t]]=t,e}),{}),O=[N.NOSCRIPT,N.SCRIPT,N.STYLE],G="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},Y=function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")},k=function(){function e(e,t){for(var n=0;n<t.length;n++){var A=t[n];A.enumerable=A.enumerable||!1,A.configurable=!0,"value"in A&&(A.writable=!0),Object.defineProperty(e,A.key,A)}}return function(t,n,A){return n&&e(t.prototype,n),A&&e(t,A),t}}(),B=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var A in n)Object.prototype.hasOwnProperty.call(n,A)&&(e[A]=n[A])}return e},W=function(e,t){var n={};for(var A in e)t.indexOf(A)>=0||Object.prototype.hasOwnProperty.call(e,A)&&(n[A]=e[A]);return n},Z=function(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t},q=function(e){var t=!(arguments.length>1&&void 0!==arguments[1])||arguments[1];return!1===t?String(e):String(e).replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;").replace(/'/g,"&#x27;")},F=function(e){var t=_(e,N.TITLE),n=_(e,U);if(n&&t)return n.replace(/%s/g,(function(){return Array.isArray(t)?t.join(""):t}));var A=_(e,P);return t||A||void 0},J=function(e){return _(e,Q)||function(){}},X=function(e,t){return t.filter((function(t){return void 0!==t[e]})).map((function(t){return t[e]})).reduce((function(e,t){return B({},e,t)}),{})},V=function(e,t){return t.filter((function(e){return void 0!==e[N.BASE]})).map((function(e){return e[N.BASE]})).reverse().reduce((function(t,n){if(!t.length)for(var A=Object.keys(n),r=0;r<A.length;r++){var u=A[r].toLowerCase();if(-1!==e.indexOf(u)&&n[u])return t.concat(n)}return t}),[])},K=function(e,t,n){var A={};return n.filter((function(t){return!!Array.isArray(t[e])||(void 0!==t[e]&&Ae("Helmet: "+e+' should be of type "Array". Instead found type "'+G(t[e])+'"'),!1)})).map((function(t){return t[e]})).reverse().reduce((function(e,n){var r={};n.filter((function(e){for(var n=void 0,u=Object.keys(e),a=0;a<u.length;a++){var i=u[a],c=i.toLowerCase();-1===t.indexOf(c)||n===g&&"canonical"===e[n].toLowerCase()||c===g&&"stylesheet"===e[c].toLowerCase()||(n=c),-1===t.indexOf(i)||i!==b&&i!==f&&i!==I||(n=i)}if(!n||!e[n])return!1;var l=e[n].toLowerCase();return A[n]||(A[n]={}),r[n]||(r[n]={}),!A[n][l]&&(r[n][l]=!0,!0)})).reverse().forEach((function(t){return e.push(t)}));for(var u=Object.keys(r),a=0;a<u.length;a++){var i=u[a],c=E()({},A[i],r[i]);A[i]=c}return e}),[]).reverse()},_=function(e,t){for(var n=e.length-1;n>=0;n--){var A=e[n];if(A.hasOwnProperty(t))return A[t]}return null},$=(A=Date.now(),function(e){var t=Date.now();t-A>16?(A=t,e(t)):setTimeout((function(){$(e)}),0)}),ee=function(e){return clearTimeout(e)},te="undefined"!=typeof window?window.requestAnimationFrame&&window.requestAnimationFrame.bind(window)||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||$:e.requestAnimationFrame||$,ne="undefined"!=typeof window?window.cancelAnimationFrame||window.webkitCancelAnimationFrame||window.mozCancelAnimationFrame||ee:e.cancelAnimationFrame||ee,Ae=function(e){return console&&"function"==typeof console.warn&&console.warn(e)},re=null,ue=function(e,t){var n=e.baseTag,A=e.bodyAttributes,r=e.htmlAttributes,u=e.linkTags,a=e.metaTags,i=e.noscriptTags,c=e.onChangeClientState,l=e.scriptTags,o=e.styleTags,s=e.title,M=e.titleAttributes;ce(N.BODY,A),ce(N.HTML,r),ie(s,M);var d={baseTag:le(N.BASE,n),linkTags:le(N.LINK,u),metaTags:le(N.META,a),noscriptTags:le(N.NOSCRIPT,i),scriptTags:le(N.SCRIPT,l),styleTags:le(N.STYLE,o)},L={},m={};Object.keys(d).forEach((function(e){var t=d[e],n=t.newTags,A=t.oldTags;n.length&&(L[e]=n),A.length&&(m[e]=d[e].oldTags)})),t&&t(),c(e,L,m)},ae=function(e){return Array.isArray(e)?e.join(""):e},ie=function(e,t){void 0!==e&&document.title!==e&&(document.title=ae(e)),ce(N.TITLE,t)},ce=function(e,t){var n=document.getElementsByTagName(e)[0];if(n){for(var A=n.getAttribute("data-react-helmet"),r=A?A.split(","):[],u=[].concat(r),a=Object.keys(t),i=0;i<a.length;i++){var c=a[i],l=t[c]||"";n.getAttribute(c)!==l&&n.setAttribute(c,l),-1===r.indexOf(c)&&r.push(c);var o=u.indexOf(c);-1!==o&&u.splice(o,1)}for(var s=u.length-1;s>=0;s--)n.removeAttribute(u[s]);r.length===u.length?n.removeAttribute("data-react-helmet"):n.getAttribute("data-react-helmet")!==a.join(",")&&n.setAttribute("data-react-helmet",a.join(","))}},le=function(e,t){var n=document.head||document.querySelector(N.HEAD),A=n.querySelectorAll(e+"[data-react-helmet]"),r=Array.prototype.slice.call(A),u=[],a=void 0;return t&&t.length&&t.forEach((function(t){var n=document.createElement(e);for(var A in t)if(t.hasOwnProperty(A))if(A===b)n.innerHTML=t.innerHTML;else if(A===f)n.styleSheet?n.styleSheet.cssText=t.cssText:n.appendChild(document.createTextNode(t.cssText));else{var i=void 0===t[A]?"":t[A];n.setAttribute(A,i)}n.setAttribute("data-react-helmet","true"),r.some((function(e,t){return a=t,n.isEqualNode(e)}))?r.splice(a,1):u.push(n)})),r.forEach((function(e){return e.parentNode.removeChild(e)})),u.forEach((function(e){return n.appendChild(e)})),{oldTags:r,newTags:u}},oe=function(e){return Object.keys(e).reduce((function(t,n){var A=void 0!==e[n]?n+'="'+e[n]+'"':""+n;return t?t+" "+A:A}),"")},se=function(e){var t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{};return Object.keys(e).reduce((function(t,n){return t[v[n]||n]=e[n],t}),t)},Me=function(e,t,n){switch(e){case N.TITLE:return{toComponent:function(){return e=t.title,n=t.titleAttributes,(A={key:e})["data-react-helmet"]=!0,r=se(n,A),[L.a.createElement(N.TITLE,r,e)];var e,n,A,r},toString:function(){return function(e,t,n,A){var r=oe(n),u=ae(t);return r?"<"+e+' data-react-helmet="true" '+r+">"+q(u,A)+"</"+e+">":"<"+e+' data-react-helmet="true">'+q(u,A)+"</"+e+">"}(e,t.title,t.titleAttributes,n)}};case T:case w:return{toComponent:function(){return se(t)},toString:function(){return oe(t)}};default:return{toComponent:function(){return function(e,t){return t.map((function(t,n){var A,r=((A={key:n})["data-react-helmet"]=!0,A);return Object.keys(t).forEach((function(e){var n=v[e]||e;if(n===b||n===f){var A=t.innerHTML||t.cssText;r.dangerouslySetInnerHTML={__html:A}}else r[n]=t[e]})),L.a.createElement(e,r)}))}(e,t)},toString:function(){return function(e,t,n){return t.reduce((function(t,A){var r=Object.keys(A).filter((function(e){return!(e===b||e===f)})).reduce((function(e,t){var r=void 0===A[t]?t:t+'="'+q(A[t],n)+'"';return e?e+" "+r:r}),""),u=A.innerHTML||A.cssText||"",a=-1===O.indexOf(e);return t+"<"+e+' data-react-helmet="true" '+r+(a?"/>":">"+u+"</"+e+">")}),"")}(e,t,n)}}}},de=function(e){var t=e.baseTag,n=e.bodyAttributes,A=e.encode,r=e.htmlAttributes,u=e.linkTags,a=e.metaTags,i=e.noscriptTags,c=e.scriptTags,l=e.styleTags,o=e.title,s=void 0===o?"":o,M=e.titleAttributes;return{base:Me(N.BASE,t,A),bodyAttributes:Me(T,n,A),htmlAttributes:Me(w,r,A),link:Me(N.LINK,u,A),meta:Me(N.META,a,A),noscript:Me(N.NOSCRIPT,i,A),script:Me(N.SCRIPT,c,A),style:Me(N.STYLE,l,A),title:Me(N.TITLE,{title:s,titleAttributes:M},A)}},Le=o()((function(e){return{baseTag:V([D,S],e),bodyAttributes:X(T,e),defer:_(e,z),encode:_(e,R),htmlAttributes:X(w,e),linkTags:K(N.LINK,[g,D],e),metaTags:K(N.META,[x,y,p,C,I],e),noscriptTags:K(N.NOSCRIPT,[b],e),onChangeClientState:J(e),scriptTags:K(N.SCRIPT,[h,b],e),styleTags:K(N.STYLE,[f],e),title:F(e),titleAttributes:X(j,e)}}),(function(e){re&&ne(re),e.defer?re=te((function(){ue(e,(function(){re=null}))})):(ue(e),re=null)}),de)((function(){return null})),me=(r=Le,a=u=function(e){function t(){return Y(this,t),Z(this,e.apply(this,arguments))}return function(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}(t,e),t.prototype.shouldComponentUpdate=function(e){return!M()(this.props,e)},t.prototype.mapNestedChildrenToProps=function(e,t){if(!t)return null;switch(e.type){case N.SCRIPT:case N.NOSCRIPT:return{innerHTML:t};case N.STYLE:return{cssText:t}}throw new Error("<"+e.type+" /> elements are self-closing and can not contain children. Refer to our API for more information.")},t.prototype.flattenArrayTypeChildren=function(e){var t,n=e.child,A=e.arrayTypeChildren,r=e.newChildProps,u=e.nestedChildren;return B({},A,((t={})[n.type]=[].concat(A[n.type]||[],[B({},r,this.mapNestedChildrenToProps(n,u))]),t))},t.prototype.mapObjectTypeChildren=function(e){var t,n,A=e.child,r=e.newProps,u=e.newChildProps,a=e.nestedChildren;switch(A.type){case N.TITLE:return B({},r,((t={})[A.type]=a,t.titleAttributes=B({},u),t));case N.BODY:return B({},r,{bodyAttributes:B({},u)});case N.HTML:return B({},r,{htmlAttributes:B({},u)})}return B({},r,((n={})[A.type]=B({},u),n))},t.prototype.mapArrayTypeChildrenToProps=function(e,t){var n=B({},t);return Object.keys(e).forEach((function(t){var A;n=B({},n,((A={})[t]=e[t],A))})),n},t.prototype.warnOnInvalidChildren=function(e,t){return!0},t.prototype.mapChildrenToProps=function(e,t){var n=this,A={};return L.a.Children.forEach(e,(function(e){if(e&&e.props){var r=e.props,u=r.children,a=function(e){var t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{};return Object.keys(e).reduce((function(t,n){return t[H[n]||n]=e[n],t}),t)}(W(r,["children"]));switch(n.warnOnInvalidChildren(e,u),e.type){case N.LINK:case N.META:case N.NOSCRIPT:case N.SCRIPT:case N.STYLE:A=n.flattenArrayTypeChildren({child:e,arrayTypeChildren:A,newChildProps:a,nestedChildren:u});break;default:t=n.mapObjectTypeChildren({child:e,newProps:t,newChildProps:a,nestedChildren:u})}}})),t=this.mapArrayTypeChildrenToProps(A,t)},t.prototype.render=function(){var e=this.props,t=e.children,n=W(e,["children"]),A=B({},n);return t&&(A=this.mapChildrenToProps(t,A)),L.a.createElement(r,A)},k(t,null,[{key:"canUseDOM",set:function(e){r.canUseDOM=e}}]),t}(L.a.Component),u.propTypes={base:c.a.object,bodyAttributes:c.a.object,children:c.a.oneOfType([c.a.arrayOf(c.a.node),c.a.node]),defaultTitle:c.a.string,defer:c.a.bool,encodeSpecialCharacters:c.a.bool,htmlAttributes:c.a.object,link:c.a.arrayOf(c.a.object),meta:c.a.arrayOf(c.a.object),noscript:c.a.arrayOf(c.a.object),onChangeClientState:c.a.func,script:c.a.arrayOf(c.a.object),style:c.a.arrayOf(c.a.object),title:c.a.string,titleAttributes:c.a.object,titleTemplate:c.a.string},u.defaultProps={defer:!0,encodeSpecialCharacters:!0},u.peek=r.peek,u.rewind=function(){var e=r.rewind();return e||(e=de({baseTag:[],bodyAttributes:{},encodeSpecialCharacters:!0,htmlAttributes:{},linkTags:[],metaTags:[],noscriptTags:[],scriptTags:[],styleTags:[],title:"",titleAttributes:{}})),e},a);me.renderStatic=me.rewind}).call(this,n("yLpj"))},rbAC:function(e,t,n){e.exports=n.p+"static/nexus-1f63e15e11e27d2899aef40cca9dc34d.png"},xfNg:function(e,t){e.exports="data:image/svg+xml;base64,PHN2ZyBpZD0ibG9nb19lcGZsXzIwMTkiIGRhdGEtbmFtZT0ibG9nbyBlcGZsIDIwMTkiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgd2lkdGg9IjE4Mi40IiBoZWlnaHQ9IjUzIiB2aWV3Qm94PSIwIDAgMTgyLjQgNTMiPjxkZWZzPjxzdHlsZT4uY2xzLTF7ZmlsbDpyZWQ7fTwvc3R5bGU+PC9kZWZzPjx0aXRsZT5lcGZsLWxvZ28tbmV3PC90aXRsZT48cG9seWdvbiBjbGFzcz0iY2xzLTEiIHBvaW50cz0iMCAyMS42IDExLjQzIDIxLjYgMTEuNDMgOS44IDM4LjM0IDkuOCAzOC4zNCAwIDAgMCAwIDIxLjYiLz48cG9seWdvbiBjbGFzcz0iY2xzLTEiIHBvaW50cz0iMCA1MyAzOC4zNCA1MyAzOC4zNCA0My4yIDExLjQzIDQzLjIgMTEuNDMgMzEuNCAwIDMxLjQgMCA1MyIvPjxyZWN0IGNsYXNzPSJjbHMtMSIgeD0iMTEuNDMiIHk9IjIxLjYiIHdpZHRoPSIyNC42MSIgaGVpZ2h0PSI5LjgiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik04Niw0Ljg3YTE2LjEyLDE2LjEyLDAsMCwwLTUuNjgtMy41M0EyMy43NiwyMy43NiwwLDAsMCw3MS44MiwwSDQ4LjE0VjUzSDU5LjU3VjMxLjRINzEuODJhMjMuNzYsMjMuNzYsMCwwLDAsOC40Ni0xLjM0QTE2LjEyLDE2LjEyLDAsMCwwLDg2LDI2LjUzYTEzLjQzLDEzLjQzLDAsMCwwLDMuMTktNSwxNy4zOCwxNy4zOCwwLDAsMCwwLTExLjYyQTEzLjUyLDEzLjUyLDAsMCwwLDg2LDQuODdaTTc4LDE4LjczYTUuNyw1LjcsMCwwLDEtMi4yNiwxLjgsMTEuMzMsMTEuMzMsMCwwLDEtMy4yNy44NSwzMiwzMiwwLDAsMS0zLjg2LjIySDU5LjU3VjkuOGg5LjA1YTMyLDMyLDAsMCwxLDMuODYuMjIsMTEsMTEsMCwwLDEsMy4yNy44NkE1LjU5LDUuNTksMCwwLDEsNzgsMTIuNjdhNSw1LDAsMCwxLC44NiwzQTUsNSwwLDAsMSw3OCwxOC43M1oiLz48cG9seWdvbiBjbGFzcz0iY2xzLTEiIHBvaW50cz0iMTU1LjQ3IDQzLjIgMTU1LjQ3IDAgMTQ0LjA0IDAgMTQ0LjA0IDUzIDE4Mi4zOCA1MyAxODIuMzggNDMuMiAxNTUuNDcgNDMuMiIvPjxwb2x5Z29uIGNsYXNzPSJjbHMtMSIgcG9pbnRzPSI5Ny40MiAyMS42IDEwOC44NSAyMS42IDEwOC44NSA5LjggMTM1Ljc2IDkuOCAxMzUuNzYgMCA5Ny40MiAwIDk3LjQyIDIxLjYiLz48cmVjdCBjbGFzcz0iY2xzLTEiIHg9Ijk3LjQyIiB5PSIzMS40IiB3aWR0aD0iMTEuNDMiIGhlaWdodD0iMjEuNiIvPjxyZWN0IGNsYXNzPSJjbHMtMSIgeD0iMTA4Ljg1IiB5PSIyMS42IiB3aWR0aD0iMjQuNjEiIGhlaWdodD0iOS44Ii8+PC9zdmc+"},yLpj:function(e,t){var n;n=function(){return this}();try{n=n||new Function("return this")()}catch(A){"object"==typeof window&&(n=window)}e.exports=n},zv5M:function(e,t){e.exports="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMy40MjYiIGhlaWdodD0iMTMuNDIzIiB2aWV3Qm94PSIwIDAgMTMuNDI2IDEzLjQyMyI+CiAgPHBhdGggaWQ9Ikljb25faW9uaWMtaW9zLWNsb3NlIiBkYXRhLW5hbWU9Ikljb24gaW9uaWMtaW9zLWNsb3NlIiBkPSJNMTkuNTg5LDE4bDQuOC00LjhBMS4xMjQsMS4xMjQsMCwwLDAsMjIuOCwxMS42MTZsLTQuOCw0LjgtNC44LTQuOEExLjEyNCwxLjEyNCwwLDEsMCwxMS42MTYsMTMuMmw0LjgsNC44LTQuOCw0LjhBMS4xMjQsMS4xMjQsMCwwLDAsMTMuMiwyNC4zODRsNC44LTQuOCw0LjgsNC44QTEuMTI0LDEuMTI0LDAsMSwwLDI0LjM4NCwyMi44WiIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTExLjI4NSAtMTEuMjg5KSIvPgo8L3N2Zz4K"}}]);
//# sourceMappingURL=commons-cb9fcbe0e2165c5204b0.js.map