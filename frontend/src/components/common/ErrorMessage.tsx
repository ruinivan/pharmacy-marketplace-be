import React from 'react';
import './ErrorMessage.css';

interface ErrorMessageProps {
  message: string;
  onRetry?: () => void;
  onDismiss?: () => void;
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({ message, onRetry, onDismiss }) => {
  return (
    <div className="error-message-container">
      <div className="error-icon">⚠️</div>
      <div className="error-content">
        <p className="error-text">{message}</p>
        <div className="error-actions">
          {onRetry && (
            <button onClick={onRetry} className="btn btn-primary btn-sm">
              Tentar Novamente
            </button>
          )}
          {onDismiss && (
            <button onClick={onDismiss} className="btn btn-secondary btn-sm">
              Fechar
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default ErrorMessage;

