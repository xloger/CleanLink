(function () {
  'use strict';

  function getPieces(text, regex) {
      const pieces = [];
      while (true) {
          const matches = regex.exec(text);
          if (!matches) {
              break;
          }
          const pieceString = matches[0];
          const piece = {
              text: pieceString,
              matches
          };
          pieces.push(piece);
      }
      return pieces;
  }
  function replacePieces(text, pieces) {
      const sortedPieces = pieces.sort((a, b) => b.matches.index - a.matches.index);
      return sortedPieces.reduce((s, c) => {
          return replace(s, c.matches, c.text);
      }, text);
  }
  function replace(text, match, replaceTo) {
      if (replaceTo === undefined || replaceTo === null) {
          return text;
      }
      const startAt = match.index;
      const endAt = match.index + match[0].length;
      return text.slice(0, startAt) + replaceTo + text.slice(endAt);
  }
  //# sourceMappingURL=pieceReplace.js.map

  function useEngine(text, rules) {
      for (const rule of rules) {
          if (rule.regex) {
              if (text.match(rule.regex)) {
                  text = text.replace(rule.regex, rule.replace);
              }
          }
          else if (rule.func) {
              text = rule.func(text);
          }
          if (rule.break) {
              break;
          }
      }
      return text;
  }
  //# sourceMappingURL=regexRules.js.map

  const URL_REGEX = /https?:\/\/[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-z0-9]{1,}\b([-a-zA-Z0-9@:%_\+.~#?&//=一-龟]*)/gi;
  function normalizeUrls(text) {
      const pieces = getPieces(text, URL_REGEX);
      for (const p of pieces) {
          p.text = transformUrlString(p.text);
          // 不正规的 URL，尝试用正则做处理
          p.text = removeSearchParamsString(p.text);
          p.text = addSpaceAround(text, p, p.text);
      }
      return replacePieces(text, pieces);
  }
  function transformUrlString(url) {
      return useEngine(url, [
          {
              // 微博国际版 -> 普通版
              regex: /^https?:\/\/fx\.weico\.net\/share\/(\d+)\.html\?weibo_id=(\d+)/g,
              replace: 'https://m.weibo.cn/status/$2',
              break: true
          }
      ]);
  }
  function addSpaceAround(originText, piece, text) {
      // 加空格
      const startAt = piece.matches.index;
      const endAt = piece.matches.index + piece.matches[0].length;
      if (startAt > 0) {
          // 前面要空格
          if (!originText[startAt - 1].match(/\s/)) {
              text = ' ' + text;
          }
      }
      if (endAt < originText.length - 1) {
          // 后面要空格
          if (!originText[endAt].match(/\s/)) {
              text = text + ' ';
          }
      }
      return text;
  }
  function removeSearchParamsString(url) {
      return useEngine(url, [
          {
              regex: /&(utm|ga)_[^=]+=[^&]+/g,
              replace: ''
          },
          {
              regex: /\?(utm|ga)_[^=]+=[^&]+/g,
              replace: '?'
          },
          {
              regex: /\?&/,
              replace: '?'
          },
          {
              regex: /&+/g,
              replace: '&'
          },
          {
              regex: /\?$/,
              replace: ''
          }
      ]);
  }

  // @ts-ignore
  Function('return this')().handlers = { clipboardText: normalizeUrls };
  //# sourceMappingURL=app.js.map

}());
//# sourceMappingURL=app.js.map
