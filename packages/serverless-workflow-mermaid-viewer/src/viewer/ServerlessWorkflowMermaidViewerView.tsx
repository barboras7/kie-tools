/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {
  Editor,
  EditorApi,
  EditorInitArgs,
  EditorTheme,
  KogitoEditorEnvelopeContextType,
} from "@kie-tools-core/editor/dist/api";
import { Notification } from "@kie-tools-core/notifications/dist/api";
import * as React from "react";
import { ServerlessWorkflowMermaidViewerChannelApi } from "../api";
import { ServerlessWorkflowMermaidViewer } from "./ServerlessWorkflowMermaidViewer";

export class ServerlessWorkflowMermaidViewerView implements Editor {
  private readonly editorRef: React.RefObject<EditorApi>;
  public af_isReact = true;
  public af_componentId: "serverless-workflow-mermaid-viewer";
  public af_componentTitle: "Serverless Workflow Mermaid Viewer";

  constructor(
    private readonly envelopeContext: KogitoEditorEnvelopeContextType<ServerlessWorkflowMermaidViewerChannelApi>,
    private readonly initArgs: EditorInitArgs
  ) {
    this.editorRef = React.createRef<EditorApi>();
  }

  public setContent(path: string, content: string): Promise<void> {
    return this.editorRef.current!.setContent(path, content);
  }

  public getContent(): Promise<string> {
    return this.editorRef.current!.getContent();
  }

  public getPreview(): Promise<string | undefined> {
    return this.editorRef.current!.getPreview();
  }

  public af_componentRoot() {
    return <ServerlessWorkflowMermaidViewer ref={this.editorRef} channelType={this.initArgs.channel} />;
  }

  public async undo(): Promise<void> {
    return this.editorRef.current!.undo();
  }

  public async redo(): Promise<void> {
    return this.editorRef.current!.redo();
  }

  public async validate(): Promise<Notification[]> {
    return this.editorRef.current!.validate();
  }

  public async setTheme(theme: EditorTheme) {
    return this.editorRef.current!.setTheme(theme);
  }
}
